import request from './request'
const baseUrl = '/user/'

/* 获取当前用户 */
export function getCurrentUser () {
  return request({
    url: baseUrl + 'getCurrentUser'
  })
}
/* 获取用户集合 */
export function listUsers () {
  return request({
    url: baseUrl + 'listUsers'
  })
}
/* 保存用户 */
export function findUser(id) {
  return request({
    url: baseUrl + 'findUser?id='+id,

  })
}
/* 保存用户 */
export function saveUser (user) {
  return request({
    url: baseUrl + 'saveUser',
    data: user
  })
}
/* 删除用户 */
export function deleteUsers (users) {
  return request({
    url: baseUrl + 'deleteUsers',
    data: users
  })
}

