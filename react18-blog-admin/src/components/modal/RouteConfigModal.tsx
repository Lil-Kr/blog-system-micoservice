import React, { useEffect, useState } from 'react'
import { Form, Input, Modal, Select, message } from 'antd'
import routeApi from '@/apis/gateway/routeApi'

const { Option } = Select

/**
 * 路由配置弹窗组件的属性
 */
interface RouteConfigModalProps {
  visible: boolean
  onClose: () => void
  onSuccess: () => void
  editData?: any
  mode: 'create' | 'edit'
}

/**
 * Schema 选项
 */
const SCHEMA_OPTIONS = [
  { label: 'HTTP', value: 'http' },
  { label: 'Dubbo', value: 'dubbo' }
]

/**
 * Method 选项
 */
const METHOD_OPTIONS = [
  { label: 'GET', value: 'GET' },
  { label: 'POST', value: 'POST' },
  { label: 'PUT', value: 'PUT' },
  { label: 'DELETE', value: 'DELETE' },
  { label: 'PATCH', value: 'PATCH' }
]

/**
 * AuthType 选项
 */
const AUTH_TYPE_OPTIONS = [
  { label: '无需认证', value: 'none' },
  { label: 'JWT', value: 'jwt' },
  { label: 'API Key', value: 'api_key' }
]

/**
 * Status 选项
 */
const STATUS_OPTIONS = [
  { label: '启用', value: 0 },
  { label: '禁用', value: 1 }
]

const RouteConfigModal: React.FC<RouteConfigModalProps> = ({ visible, onClose, onSuccess, editData, mode }) => {
  const [form] = Form.useForm()
  const [loading, setLoading] = useState(false)
  const [appNameOptions, setAppNameOptions] = useState<Array<{ value: string; label: string }>>([])

  /**
   * 加载 appName 列表
   */
  const loadAppNameList = async () => {
    try {
      const res = await routeApi.list({})
      if (res.code === 200 && res.data) {
        // 提取所有唯一的 appName
        const appNameSet = new Set<string>()
        res.data.forEach((item: any) => {
          if (item.appName) {
            appNameSet.add(item.appName)
          }
        })
        const options = Array.from(appNameSet).map(name => ({ value: name, label: name }))
        setAppNameOptions(options)
      }
    } catch (error) {
      console.error('Failed to load appName list:', error)
    }
  }

  /**
   * 提交表单
   */
  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      setLoading(true)
      let res
      if (mode === 'create') {
        const createData = {
          ...values,
          status: values.status ?? 0
        }
        console.log('创建请求数据:', createData)
        res = await routeApi.create(createData)
      } else {
        const editDataReq = {
          ...values,
          id: editData?.id,
          status: values.status ?? 0
        }
        res = await routeApi.edit(editDataReq)
      }

      if (res.code === 200) {
        message.success(mode === 'create' ? '创建成功' : '编辑成功')
        form.resetFields()
        onSuccess()
        onClose()
      } else {
        message.error(res.msg || (mode === 'create' ? '创建失败' : '编辑失败'))
      }
    } catch (error) {
      console.error('Submit error:', error)
      message.error('操作失败')
    } finally {
      setLoading(false)
    }
  }

  /**
   * 取消
   */
  const handleCancel = () => {
    form.resetFields()
    onClose()
  }

  /**
   * 弹窗打开时初始化数据
   */
  useEffect(() => {
    if (visible) {
      loadAppNameList()
      if (mode === 'edit' && editData) {
        form.setFieldsValue({
          appName: editData.appName,
          schema: editData.schema,
          method: editData.method,
          path: editData.path,
          uri: editData.uri,
          dubboInvokeParamClass: editData.dubboInvokeParamClass,
          authType: editData.authType,
          status: editData.status
        })
      } else {
        form.setFieldsValue({
          status: 0,
          schema: 'http',
          method: 'GET',
          authType: 'none'
        })
      }
    }
  }, [visible, mode, editData])

  return (
    <Modal
      title={mode === 'create' ? '新增路由配置' : '编辑路由配置'}
      open={visible}
      onOk={handleSubmit}
      onCancel={handleCancel}
      confirmLoading={loading}
      width={700}
      centered
      destroyOnClose
    >
      <Form form={form} layout='horizontal' labelCol={{ span: 6 }} wrapperCol={{ span: 18 }} autoComplete='off'>
        <Form.Item label='服务名' name='appName' rules={[{ required: true, message: '请输入服务名' }]}>
          <Select
            showSearch
            placeholder='请选择或输入服务名'
            options={appNameOptions}
            filterOption={(input, option) => (option?.label ?? '').toLowerCase().includes(input.toLowerCase())}
            allowClear
          />
        </Form.Item>

        <Form.Item label='协议类型' name='schema' rules={[{ required: true, message: '请选择协议类型' }]}>
          <Select placeholder='请选择协议类型'>
            {SCHEMA_OPTIONS.map(opt => (
              <Option key={opt.value} value={opt.value}>
                {opt.label}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item label='请求方法' name='method' rules={[{ required: true, message: '请选择请求方法' }]}>
          <Select placeholder='请选择请求方法'>
            {METHOD_OPTIONS.map(opt => (
              <Option key={opt.value} value={opt.value}>
                {opt.label}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item label='网关路径' name='path' rules={[{ required: true, message: '请输入网关路径' }]}>
          <Input placeholder='例如: /api/user/info' />
        </Form.Item>

        <Form.Item
          label='目标URI'
          name='uri'
          rules={[{ required: true, message: '请输入目标URI' }]}
          tooltip='HTTP: lb://app-name, Dubbo: 接口全路径'
        >
          <Input placeholder='例如: lb://user-service' />
        </Form.Item>

        <Form.Item label='认证类型' name='authType' rules={[{ required: true, message: '请选择认证类型' }]}>
          <Select placeholder='请选择认证类型'>
            {AUTH_TYPE_OPTIONS.map(opt => (
              <Option key={opt.value} value={opt.value}>
                {opt.label}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item label='状态' name='status' rules={[{ required: true, message: '请选择状态' }]}>
          <Select placeholder='请选择状态'>
            {STATUS_OPTIONS.map(opt => (
              <Option key={opt.value} value={opt.value}>
                {opt.label}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item label='Dubbo参数类' name='dubboInvokeParamClass' tooltip='Dubbo 路由时使用，可选填'>
          <Input placeholder='例如: com.example.UserRequest' />
        </Form.Item>
      </Form>
    </Modal>
  )
}

export default RouteConfigModal
