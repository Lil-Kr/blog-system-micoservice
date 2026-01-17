import React, { useEffect, useRef, useState } from 'react'
import { CopyOutlined, DeleteOutlined, EditOutlined, PlusOutlined, ReloadOutlined, SearchOutlined } from '@ant-design/icons'
import { Button, Card, Flex, Input, Popconfirm, Select, Space, Table, Tag, Tooltip, Typography } from 'antd'
import { PaginationProps } from 'antd/lib'
import { ColumnsType } from 'antd/lib/table'
import {
  RouteConfigTableType,
  RouteConfigListPageReq,
  RouteConfigAddReq,
  RouteConfigEditReq
} from '@/types/apis/gateway/routeType'
import routeApi from '@/apis/gateway/routeApi'
import { useMessage } from '@/components/message/MessageProvider'
import { useGlobalStyleStore } from '@/store/global/globalStore'
import { transformRouteConfigToTable } from '@/utils/sys/transform'
import { TableRowSelection } from 'antd/es/table/interface'
import { TablePageInfoType } from '@/types/base'
import RouteConfigModal from '@/components/modal/RouteConfigModal'

const { Title } = Typography

const ApiRoute = () => {
  const messageApi = useMessage()
  const { btnSize, tableSize } = useGlobalStyleStore()

  const [tableLoading, setTableLoading] = useState<boolean>(true)
  const [dataSource, setDataSource] = useState<RouteConfigTableType[]>([])

  const [tablePageInfo, setTablePageInfo] = useState<TablePageInfoType>({
    currentPageNum: 1,
    pageSize: 20,
    totalSize: 0
  })

  // Modal 状态
  const [modalVisible, setModalVisible] = useState(false)
  const [modalMode, setModalMode] = useState<'create' | 'edit'>('create')
  const [editData, setEditData] = useState<any>(null)

  // 搜索状态
  const [searchAppName, setSearchAppName] = useState<string>('')
  const [searchStatus, setSearchStatus] = useState<number | undefined>(undefined)

  // 状态选项
  const statusOptions = [
    { label: '启用', value: 0 },
    { label: '禁用', value: 1 }
  ]

  // 使用 ref 来保存最新的分页信息，避免闭包陷阱
  const tablePageInfoRef = useRef(tablePageInfo)
  useEffect(() => {
    tablePageInfoRef.current = tablePageInfo
  }, [tablePageInfo])

  /**
   * 复制文本到剪贴板
   */
  const handleCopy = async (text: string, fieldName: string) => {
    try {
      await navigator.clipboard.writeText(text)
      messageApi?.success(`${fieldName}已复制到剪贴板`)
    } catch (error) {
      messageApi?.error('复制失败，请手动复制')
      console.error('Copy failed:', error)
    }
  }

  /**
   * 路由配置列表的列配置
   */
  const columns: ColumnsType<RouteConfigTableType> = [
    {
      key: 'appName',
      dataIndex: 'appName',
      title: '服务名',
      width: '15%'
    },
    {
      key: 'schema',
      dataIndex: 'schema',
      title: '协议类型',
      width: '10%',
      render: (_, record) => {
        const color = record.schema === 'http' ? 'blue' : 'green'
        return <Tag color={color}>{record.schema?.toUpperCase()}</Tag>
      }
    },
    {
      key: 'method',
      dataIndex: 'method',
      title: '请求方法',
      width: '10%',
      render: (_, record) => {
        const method = record.method?.toUpperCase() || 'GET'
        let color = 'default'
        switch (method) {
          case 'GET':
            color = 'green'
            break
          case 'POST':
            color = 'blue'
            break
          case 'PUT':
            color = 'orange'
            break
          case 'DELETE':
            color = 'red'
            break
          default:
            color = 'default'
        }
        return <Tag color={color}>{method}</Tag>
      }
    },
    {
      key: 'path',
      dataIndex: 'path',
      title: '网关路径',
      width: '20%',
      ellipsis: true,
      render: (_, record) => (
        <Space size='small'>
          <span style={{ wordBreak: 'break-all' }}>{record.path}</span>
          <Tooltip title='复制路径'>
            <Button
              type='link'
              size='small'
              icon={<CopyOutlined />}
              onClick={() => handleCopy(record.path || '', '网关路径')}
              style={{ padding: '0 4px', color: '#1890ff' }}
            />
          </Tooltip>
        </Space>
      )
    },
    {
      key: 'uri',
      dataIndex: 'uri',
      title: '目标URI',
      width: '20%',
      ellipsis: true,
      render: (_, record) => (
        <Space size='small'>
          <span style={{ wordBreak: 'break-all' }}>{record.uri}</span>
          <Tooltip title='复制URI'>
            <Button
              type='link'
              size='small'
              icon={<CopyOutlined />}
              onClick={() => handleCopy(record.uri || '', '目标URI')}
              style={{ padding: '0 4px', color: '#1890ff' }}
            />
          </Tooltip>
        </Space>
      )
    },
    {
      key: 'authType',
      dataIndex: 'authType',
      title: '认证类型',
      width: '10%',
      render: (_, record) => {
        const authTypeMap: Record<string, { text: string; color: string }> = {
          none: { text: '无需认证', color: 'green' },
          jwt: { text: 'JWT', color: 'blue' },
          api_key: { text: 'API Key', color: 'orange' }
        }
        const authInfo = authTypeMap[record.authType] || { text: record.authType, color: 'default' }
        return <Tag color={authInfo.color}>{authInfo.text}</Tag>
      }
    },
    {
      key: 'status',
      dataIndex: 'status',
      title: '状态',
      width: '10%',
      render: (_, record) => {
        const statusText = record.status === 0 ? '启用' : '禁用'
        const color = record.status === 0 ? 'green' : 'red'
        return <Tag color={color}>{statusText}</Tag>
      }
    },
    {
      key: 'operation',
      title: '操作',
      dataIndex: 'operation',
      width: '15%',
      render: (_, record) => (
        <Space size='small'>
          <Button
            type='link'
            size={btnSize === 'middle' ? 'small' : btnSize}
            icon={<EditOutlined />}
            onClick={() => handleEdit(record)}
          >
            编辑
          </Button>
          <Popconfirm
            title='删除路由配置'
            description={`确定要删除路由 [${record.path}] 吗?`}
            onConfirm={() => handleDelete(record)}
            okText='确定'
            cancelText='取消'
          >
            <Button type='link' size={btnSize === 'middle' ? 'small' : btnSize} danger icon={<DeleteOutlined />}>
              删除
            </Button>
          </Popconfirm>
        </Space>
      )
    }
  ]

  /**
   * 初始化数据
   */
  useEffect(() => {
    fetchRouteConfigList()
  }, [])

  /**
   * 查询路由配置列表
   */
  const fetchRouteConfigList = async (params?: Partial<RouteConfigListPageReq>) => {
    setTableLoading(true)
    try {
      const req: RouteConfigListPageReq = {
        currentPageNum: params?.currentPageNum || tablePageInfo.currentPageNum,
        pageSize: params?.pageSize || tablePageInfo.pageSize,
        appName: params?.appName,
        schema: params?.schema,
        method: params?.method,
        path: params?.path,
        uri: params?.uri,
        status: params?.status
      }
      const res = await routeApi.pageList(req)
      const { code, data } = res
      if (code !== 200) {
        messageApi?.error('查询路由配置列表失败')
        return
      }
      const tableList = transformRouteConfigToTable(data.list || [])
      setDataSource(tableList)

      // 容错处理：如果后端没有返回 total，保持之前的值
      const newTotal = data.total !== undefined && data.total !== null ? data.total : tablePageInfo.totalSize

      setTablePageInfo(prevState => ({
        ...prevState,
        currentPageNum: req.currentPageNum,
        pageSize: req.pageSize,
        totalSize: newTotal
      }))
    } catch (error) {
      messageApi?.error('查询路由配置列表异常')
    } finally {
      setTableLoading(false)
    }
  }

  /**
   * 分页变化
   */
  const onChangePageInfo: PaginationProps['onChange'] = (currentPageNum, pageSize) => {
    fetchRouteConfigList({ currentPageNum, pageSize, appName: searchAppName, status: searchStatus })
  }

  /**
   * 搜索处理
   */
  const handleSearch = () => {
    fetchRouteConfigList({
      currentPageNum: 1,
      pageSize: tablePageInfo.pageSize,
      appName: searchAppName,
      status: searchStatus
    })
  }

  /**
   * 重置搜索
   */
  const handleResetSearch = () => {
    setSearchAppName('')
    setSearchStatus(undefined)
    fetchRouteConfigList({
      currentPageNum: 1,
      pageSize: tablePageInfo.pageSize,
      appName: '',
      status: undefined
    })
  }

  /**
   * 编辑路由配置
   */
  const handleEdit = (record: RouteConfigTableType) => {
    setModalMode('edit')
    setEditData(record)
    setModalVisible(true)
  }

  /**
   * 删除路由配置
   */
  const handleDelete = async (record: RouteConfigTableType) => {
    try {
      const res = await routeApi.delete({ id: record.id })
      const { code, msg } = res
      if (code !== 200) {
        messageApi?.error(msg || '删除路由配置失败')
        return
      }
      messageApi?.success(msg || '删除路由配置成功')
      // 使用 ref 获取最新的分页信息，避免闭包陷阱
      fetchRouteConfigList({
        currentPageNum: tablePageInfoRef.current.currentPageNum,
        pageSize: tablePageInfoRef.current.pageSize,
        appName: searchAppName,
        status: searchStatus
      })
    } catch (error) {
      messageApi?.error('删除路由配置异常')
      console.error('handleDelete error:', error)
    }
  }

  /**
   * 新增路由配置
   */
  const handleAdd = () => {
    setModalMode('create')
    setEditData(null)
    setModalVisible(true)
  }

  /**
   * Modal 成功回调
   */
  const handleModalSuccess = () => {
    fetchRouteConfigList({
      currentPageNum: tablePageInfoRef.current.currentPageNum,
      pageSize: tablePageInfoRef.current.pageSize,
      appName: searchAppName,
      status: searchStatus
    })
  }

  /**
   * Modal 关闭回调
   */
  const handleModalClose = () => {
    setModalVisible(false)
    setEditData(null)
  }

  /**
   * 表格为checkbox时启用
   */
  const rowSelection: TableRowSelection<RouteConfigTableType> = {
    onChange: (selectedRowKeys, selectedRows) => {},
    onSelect: (record, selected, selectedRows) => {},
    onSelectAll: (selected, selectedRows, changeRows) => {}
  }

  return (
    <div className='route-config-wrapper' style={{ height: '100%', padding: '16px', overflow: 'hidden' }}>
      <Flex gap='middle' vertical={true} style={{ height: '100%' }}>
        <Flex justify='space-between' align='center' style={{ flexShrink: 0 }}>
          <Flex gap='small' align='center'>
            <Input
              placeholder="搜索服务名"
              value={searchAppName}
              onChange={(e) => setSearchAppName(e.target.value)}
              onPressEnter={handleSearch}
              style={{ width: 200 }}
              allowClear
            />
            <Select
              placeholder="选择状态"
              value={searchStatus}
              onChange={setSearchStatus}
              style={{ width: 120 }}
              options={statusOptions}
              allowClear
            />
            <Button icon={<SearchOutlined />} onClick={handleSearch}>
              {'搜索'}
            </Button>
            <Button onClick={handleResetSearch}>
              {'重置'}
            </Button>
          </Flex>
          <Flex gap='small'>
            <Tooltip title='刷新'>
              <Button
                icon={<ReloadOutlined />}
                onClick={() =>
                  fetchRouteConfigList({
                    currentPageNum: 1,
                    pageSize: tablePageInfoRef.current.pageSize,
                    appName: searchAppName,
                    status: searchStatus
                  })
                }
              />
            </Tooltip>
            <Button type='primary' icon={<PlusOutlined />} onClick={handleAdd}>
              {'新增路由'}
            </Button>
          </Flex>
        </Flex>
        <div style={{ flex: 1, overflow: 'hidden' }}>
          <Table<RouteConfigTableType>
            key={1}
            size={tableSize}
            bordered={true}
            loading={tableLoading}
            columns={columns}
            dataSource={dataSource}
            rowSelection={{
              type: 'checkbox',
              ...rowSelection
            }}
            scroll={{ x: 'max-content', y: 'calc(100vh - 220px)' }}
            pagination={{
              position: ['bottomLeft'],
              showQuickJumper: false,
              showSizeChanger: true,
              hideOnSinglePage: false,
              pageSizeOptions: [10, 20, 50],
              onChange: onChangePageInfo,
              current: tablePageInfo.currentPageNum,
              pageSize: tablePageInfo.pageSize,
              total: tablePageInfo.totalSize
            }}
          />
        </div>
      </Flex>

      {/* 路由配置弹窗 */}
      <RouteConfigModal
        visible={modalVisible}
        onClose={handleModalClose}
        onSuccess={handleModalSuccess}
        editData={editData}
        mode={modalMode}
      />
    </div>
  )
}

export default ApiRoute
