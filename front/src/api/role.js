import request from './request'
const baseUrl = '/role/'

/* 获取用户集合 */
export function listRoles () {
  return request({
    url: baseUrl + 'listRoles'
  })
}
