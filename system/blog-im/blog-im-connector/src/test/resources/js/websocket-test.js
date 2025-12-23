let ws = null;
let heartbeatTimer = null;
let reconnecting = false;
const RECONNECT_TIMEOUT = 5000;
const logContent = document.getElementById('logContent');
const connectBtn = document.getElementById('connectBtn');
const disconnectBtn = document.getElementById('disconnectBtn');
const sendBtn = document.getElementById('sendBtn');

// 格式化日志时间
function formatTime() {
    return new Date().toLocaleString('zh-CN', {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit', second: '2-digit',
        hour12: false
    });
}

// 添加日志
function addLog(type, content) {
    const time = formatTime();
    const logItem = document.createElement('div');
    logItem.className = `log-${type}`;
    logItem.innerHTML = `<span>[${time}]</span> - <span>${content}</span>`;
    logContent.appendChild(logItem);
    logContent.scrollTop = logContent.scrollHeight;
}

// 添加自定义 HTTP 头输入框
function addHeader() {
    const headerList = document.getElementById('headerList');
    const headerItem = document.createElement('div');
    headerItem.className = 'header-item';
    headerItem.innerHTML = `
        <input type="text" class="header-key" placeholder="头名称（如 X-Token）">
        <input type="text" class="header-value" placeholder="头值（如 xxx-xxx-xxx）">
        <button class="btn-remove-header" onclick="removeHeader(this)">删除</button>
    `;
    headerList.appendChild(headerItem);
}

// 删除自定义 HTTP 头
function removeHeader(btn) {
    const headerItem = btn.parentElement;
    if (document.querySelectorAll('.header-item').length > 1) {
        headerItem.remove();
    } else {
        addLog('error', '至少保留 1 组头信息');
    }
}

// 复用的消息处理函数（统一处理新/旧连接的消息）
function handleWsMessage(event) {
    try {
        const msgObj = JSON.parse(event.data);
        if (msgObj.code == 1301) {
            addLog('in', `收到MOVE信号：${event.data}`);
            reconnectWebSocket();
        } else {
            addLog('in', `收到服务端消息：${event.data}`);
        }
    } catch (e) {
        addLog('error', `消息解析失败：${e.message}，原始消息：${event.data}`);
        addLog('in', `收到服务端消息：${event.data}`);
    }
}

// 彻底释放旧连接的工具函数
function releaseOldWebSocket(oldWsInstance) {
    if (!oldWsInstance) return;

    addLog('debug', '开始彻底释放旧WebSocket连接资源...');

    // 清空所有事件监听
    oldWsInstance.onopen = null;
    oldWsInstance.onmessage = null;
    oldWsInstance.onclose = null;
    oldWsInstance.onerror = null;

    // 判断连接状态，确保真正关闭
    if (oldWsInstance.readyState === WebSocket.OPEN) {
        try {
            const closeFuture = oldWsInstance.close(1001, "1301信号重连成功，主动关闭旧连接");
            closeFuture.then(() => {
                addLog('info', '旧WebSocket连接已彻底关闭（closeFuture完成）');
            }).catch((e) => {
                addLog('warn', `旧连接关闭时触发异常：${e.message}`);
            });
        } catch (e) {
            addLog('warn', `关闭旧连接失败：${e.message}`);
        }
    } else if (oldWsInstance.readyState === WebSocket.CLOSING) {
        addLog('info', '旧WebSocket连接正在关闭，等待完成...');
    } else {
        addLog('info', '旧WebSocket连接已关闭，无需重复操作');
    }

    // 强制解除引用
    setTimeout(() => {
        oldWsInstance = null;
    }, 500);

    addLog('debug', '旧WebSocket连接资源释放流程执行完毕');
}

// 清理心跳定时器
function clearHeartbeatTimer() {
    if (heartbeatTimer) {
        clearInterval(heartbeatTimer);
        heartbeatTimer = null;
    }
}

// 启动心跳（绑定全局ws）
function startHeartbeat() {
    clearHeartbeatTimer();
    heartbeatTimer = setInterval(() => {
        if (ws && ws.readyState === WebSocket.OPEN) {
            try {
                ws.send(JSON.stringify({
                    body:[123,34,99,111,110,116,101,110,116,34,58,34,112,105,110,103,34,125],
                    code:1101,
                    len:18,
                    magic:0
                }));
            } catch (e) {
                addLog('warn', `心跳发送失败：${e.message}`);
            }
        }
    }, 5000);
    addLog('info', '心跳定时器已启动');
}

// 重连WebSocket核心逻辑
function reconnectWebSocket() {
    if (reconnecting) {
        addLog('warn', '已有重连操作正在进行，跳过本次触发');
        return;
    }
    reconnecting = true;

    addLog('warn', '收到1301 MOVE信号，开始执行重连逻辑（保留旧连接兜底）...');

    const oldWs = ws;
    const isOldWsAlive = oldWs && oldWs.readyState === WebSocket.OPEN;
    if (isOldWsAlive) {
        addLog('info', '旧WebSocket连接仍存活，重连期间保留旧连接');
    }

    const wsUrl = document.getElementById('wsUrl').value.trim();
    if (!wsUrl) {
        addLog('error', '重连失败：WebSocket地址为空，无法重连');
        reconnecting = false;
        return;
    }

    let newWs = null;
    let reconnectTimeoutTimer = null;
    try {
        newWs = new WebSocket(wsUrl);
        // 关键：提前绑定消息监听（避免连接成功后漏收消息）
        newWs.onmessage = handleWsMessage;
        // 绑定错误监听
        newWs.onerror = function(error) {
            clearTimeout(reconnectTimeoutTimer);
            addLog('error', `新WebSocket连接错误：${error.message || JSON.stringify(error)}`);
            addLog('warn', '重连失败，继续使用旧WebSocket连接（若仍存活）');
            newWs = null;
            reconnecting = false;
        };
    } catch (e) {
        addLog('error', `重连失败：创建新WebSocket实例失败 - ${e.message}`);
        reconnecting = false;
        return;
    }

    reconnectTimeoutTimer = setTimeout(() => {
        addLog('error', `重连失败：超过${RECONNECT_TIMEOUT/1000}秒仍未连接成功`);
        if (newWs) {
            // 清空所有监听再关闭
            newWs.onopen = null;
            newWs.onmessage = null;
            newWs.onclose = null;
            newWs.onerror = null;
            try {
                newWs.close(1006, "重连超时");
            } catch (e) {
                addLog('warn', `关闭超时新连接失败：${e.message}`);
            }
            newWs = null;
        }
        reconnecting = false;
    }, RECONNECT_TIMEOUT);

    // 新ws连接成功回调
    newWs.onopen = function() {
        clearTimeout(reconnectTimeoutTimer);
        addLog('info', '新WebSocket连接成功！开始切换连接...');
        // 【新增】先强制解锁按钮（兜底）
        connectBtn.disabled = true;
        disconnectBtn.disabled = false;
        sendBtn.disabled = false;
        // 校验新连接状态
        if (newWs.readyState !== WebSocket.OPEN) {
            addLog('error', '新连接状态异常，切换失败');
            reconnecting = false;
            return;
        }

        // 向旧连接发送回应信号
        if (oldWs && oldWs.readyState === WebSocket.OPEN) {
            try {
                //发送关闭信号
                const switchAckMsg = JSON.stringify({
                    code: 1002,
                    body: [123,34,99,108,111,115,101,83,116,97,116,117,115,34,58,49,125],
                    len: 17,
                    magic: 0
                });
                oldWs.send(switchAckMsg);
                addLog('out', `向旧连接发送切换确认信号：${switchAckMsg}`);

                // 延迟释放旧连接（延长至1秒，确保信号发送完成）
                setTimeout(() => {
                    releaseOldWebSocket(oldWs);
                }, 1000);
            } catch (e) {
                releaseOldWebSocket(oldWs);
            }
        }

        // 替换全局ws为新ws
        ws = newWs;
        // 重启心跳（绑定全局ws）
        // startHeartbeat();
        // 同步UI状态
        connectBtn.disabled = true;
        disconnectBtn.disabled = false;
        sendBtn.disabled = false;

        addLog('success', 'WebSocket连接切换完成！已使用新连接');
        reconnecting = false;
    };

    // 新ws关闭回调
    newWs.onclose = function(event) {
        clearTimeout(reconnectTimeoutTimer);
        addLog('error', `新WebSocket连接关闭：代码${event.code}，原因${event.reason}`);
        addLog('warn', '重连失败，继续使用旧WebSocket连接（若仍存活）');
        // 若旧连接已死，更新UI
        if (!isOldWsAlive) {
            addLog('error', '旧连接也已关闭，请手动点击"连接"按钮重新建立连接');
            connectBtn.disabled = false;
            disconnectBtn.disabled = true;
            sendBtn.disabled = true;
            clearHeartbeatTimer();
        }
        newWs = null;
        reconnecting = false;
    };
}

// 连接 WebSocket
function connectWebSocket() {
    const wsUrl = document.getElementById('wsUrl').value.trim();
    if (!wsUrl) {
        addLog('error', '请输入有效的 WebSocket 连接地址');
        return;
    }

    // 连接前先彻底释放旧连接
    if (ws) {
        releaseOldWebSocket(ws);
    }

    try {
        addLog('info', `准备连接：${wsUrl}`);

        if (typeof WebSocket !== 'undefined') {
            ws = new WebSocket(wsUrl);
        } else {
            addLog('error', '当前浏览器不支持 WebSocket');
            return;
        }

        ws.onopen = function() {
            addLog('info', 'WebSocket 连接成功！');
            connectBtn.disabled = true;
            disconnectBtn.disabled = false;
            sendBtn.disabled = false;
            // 启动心跳
            // startHeartbeat();
        };

        // 绑定消息处理
        ws.onmessage = handleWsMessage;

        ws.onclose = function(event) {
            addLog('info', `WebSocket 连接关闭，代码：${event.code}，原因：${event.reason}`);
            connectBtn.disabled = false;
            disconnectBtn.disabled = true;
            sendBtn.disabled = true;
            clearHeartbeatTimer();
            ws = null;
        };

        ws.onerror = function(error) {
            addLog('error', `WebSocket 错误：${error.message || JSON.stringify(error)}`);
        };

    } catch (error) {
        addLog('error', `连接失败：${error.message}`);
    }
}

// 断开 WebSocket 连接
function disconnectWebSocket() {
    if (ws) {
        addLog('info', '手动断开WebSocket连接');
        releaseOldWebSocket(ws);
        ws = null;
    }
    clearHeartbeatTimer();
    // 更新UI
    connectBtn.disabled = false;
    disconnectBtn.disabled = true;
    sendBtn.disabled = true;
}

// 手动发送消息
function sendMessage() {
    if (!ws || ws.readyState !== WebSocket.OPEN) {
        addLog('error', 'WebSocket 未连接或已断开');
        return;
    }

    const message = document.getElementById('sendMessage').value.trim();
    if (!message) {
        addLog('error', '请输入要发送的消息内容');
        return;
    }

    try {
        ws.send(message);
        addLog('out', `发送消息：${message}`);
    } catch (error) {
        addLog('error', `发送失败：${error.message}`);
    }
}

// 批量发送消息
function batchSendMessage() {
    if (!ws || ws.readyState !== WebSocket.OPEN) {
        addLog('error', 'WebSocket 未连接或已断开');
        return;
    }

    const message = document.getElementById('sendMessage').value.trim();
    if (!message) {
        addLog('error', '请输入要发送的消息内容');
        return;
    }

    const batchCount = parseInt(document.getElementById('batchCount').value);
    if (isNaN(batchCount) || batchCount < 1 || batchCount > 100) {
        addLog('error', '发送条数必须是 1-100 之间的数字');
        return;
    }

    addLog('info', `开始批量发送 ${batchCount} 条消息...`);

    for (let i = 0; i < batchCount; i++) {
        setTimeout(() => {
            if (ws && ws.readyState === WebSocket.OPEN) {
                const batchMessage = message.replace(/\{index\}/g, i + 1);
                try {
                    ws.send(batchMessage);
                    addLog('out', `批量发送（${i + 1}/${batchCount}）：${batchMessage}`);
                } catch (e) {
                    addLog('error', `批量发送失败（${i + 1}/${batchCount}）：${e.message}`);
                }
            } else {
                addLog('error', `批量发送中断（${i + 1}/${batchCount}）：连接已断开`);
            }
        }, i * 100);
    }
}

// 页面关闭时自动断开连接
window.onbeforeunload = function() {
    if (ws) {
        releaseOldWebSocket(ws);
    }
    clearHeartbeatTimer();
};