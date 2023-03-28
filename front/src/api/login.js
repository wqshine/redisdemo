import request from './request'
/* 登录 */
export function login (param) {
  return request({
    url: '/login?username=' + param.username + '&password=' + param.password + '&remember-me=' + param.rememberMe
  })
}

/* 登出 */
export function logout () {
  return request({
    url: '/logout'
  })
}
/* 修改密码 */
export function changePassword (param) {
  return request({
    url: '/user/changePassword?oldPassword=' + param.oldPassword + '&newPassword=' + param.newPassword + '&repeatPassword=' + param.repeatPassword
  })
}
