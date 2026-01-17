import { PREFIX_URL_ROUTE_CONFIG } from '@/config'
import {
  RouteConfig,
  RouteConfigApi,
  RouteConfigAddReq,
  RouteConfigDelReq,
  RouteConfigEditReq,
  RouteConfigGetReq,
  RouteConfigListPageReq,
  RouteConfigQueryReq,
  RouteChangeLog
} from '@/types/apis/gateway/routeType'
import { Result, ResultPage } from '@/types/base/response'
import { baseAxiosRequest } from '@/utils/http/request'

const routeApi: RouteConfigApi = {
  pageList(req: RouteConfigListPageReq) {
    return baseAxiosRequest.post<ResultPage<RouteConfig>>(PREFIX_URL_ROUTE_CONFIG + '/pageList', req)
  },
  list(req: RouteConfigQueryReq) {
    return baseAxiosRequest.post<Result<RouteConfig[]>>(PREFIX_URL_ROUTE_CONFIG + '/list', req)
  },
  create(req: RouteConfigAddReq) {
    return baseAxiosRequest.post<Result<string>>(PREFIX_URL_ROUTE_CONFIG + '/create', req)
  },
  edit(req: RouteConfigEditReq) {
    return baseAxiosRequest.post<Result<string>>(PREFIX_URL_ROUTE_CONFIG + '/edit', req)
  },
  delete(req: RouteConfigDelReq) {
    return baseAxiosRequest.delete<Result<string>>(PREFIX_URL_ROUTE_CONFIG + '/delete', req)
  },
  getConfigLog(req: RouteConfigGetReq) {
    return baseAxiosRequest.get<Result<RouteChangeLog[]>>(PREFIX_URL_ROUTE_CONFIG + '/getConfigLog', {
      params: req
    })
  }
}

export default routeApi
