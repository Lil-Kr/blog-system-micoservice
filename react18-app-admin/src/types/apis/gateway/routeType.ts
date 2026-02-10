import { Result, ResultPage } from '@/types/base/response'
import { BaseApi, OptionType } from '@/types/apis/'
import { BaseEntityPageType, BaseEntityType } from '@/types/base'

/**
 * 路由配置表格类型
 */
export interface RouteConfigTableType {
  key: string
  id: string
  appName: string
  schema: string
  method: string
  path: string
  uri: string
  dubboInvokeParamClass?: string
  authType: string
  status: number
  createTime?: string
  updateTime?: string
}

/**
 * 路由配置实体
 */
export interface RouteConfig extends BaseEntityType {
  id: string
  appName: string
  schema: string
  method: string
  path: string
  uri: string
  dubboInvokeParamClass?: string
  authType: string
  status: number
}

/**
 * 路由配置分页查询请求
 */
export interface RouteConfigListPageReq extends BaseEntityPageType {
  appName?: string
  schema?: string
  method?: string
  path?: string
  uri?: string
  status?: number
}

/**
 * 路由配置查询请求
 */
export interface RouteConfigQueryReq {
  appName?: string
  schema?: string
  method?: string
  path?: string
  uri?: string
}

/**
 * 路由配置添加请求
 */
export interface RouteConfigAddReq {
  appName: string
  schema: string
  method: string
  path: string
  uri: string
  dubboInvokeParamClass?: string
  authType: string
  status: number
}

/**
 * 路由配置编辑请求
 */
export interface RouteConfigEditReq {
  id: string
  appName: string
  schema: string
  method: string
  path: string
  uri: string
  dubboInvokeParamClass?: string
  authType: string
  status: number
}

/**
 * 路由配置删除请求
 */
export interface RouteConfigDelReq {
  id: string
}

/**
 * 路由配置获取日志请求
 */
export interface RouteConfigGetReq {
  configId: string
}

/**
 * 路由变更日志
 */
export interface RouteChangeLog {
  id: string
  configId: string
  operationType: string
  beforeConfig: string
  afterConfig: string
  operatorId: string
  operatorName: string
  createTime: string
}

/**
 * 路由配置 API 接口定义
 */
export interface RouteConfigApi extends BaseApi {
  pageList(req: RouteConfigListPageReq): Promise<ResultPage<RouteConfig>>
  list(req: RouteConfigQueryReq): Promise<Result<RouteConfig[]>>
  create(req: RouteConfigAddReq): Promise<Result<string>>
  edit(req: RouteConfigEditReq): Promise<Result<string>>
  delete(req: RouteConfigDelReq): Promise<Result<string>>
  getConfigLog(req: RouteConfigGetReq): Promise<Result<RouteChangeLog[]>>
}
