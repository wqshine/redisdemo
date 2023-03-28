import axios from 'axios'
// import { getToken } from '@/utils/auth'

// create an axios instance
let service = axios.create({
  // 外网更新
  // baseURL: 'http://xxxxx',
  baseURL: '/redisdemo',
  timeout: 5000, // 请求超时时间
  method: 'post' // 默认都是用post方式提交
})

const convertToJson = function (obj) {
  // Generate a random value structured as a GUID
  const guid = function () {
    function s4 () {
      return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1)
    }

    return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4()
  }
  // Check if a value is an object
  const isObject = function (value) {
    // 上面这个不能判断value为null的情况
    // return (typeof value === 'object');
    return value === Object(value)
  }
  // Check if an object is an array
  const isArray = function (obj) {
    return (Object.prototype.toString.call(obj) === '[object Array]')
  }
  const convertToJsonHelper = function (obj, key, objects) {
    // Initialize objects array and
    // put root object into if it exist
    if (!objects) {
      objects = []

      if (isObject(obj) && (!isArray(obj))) {
        if (!obj[key]) { obj[key] = guid() }
        objects.push(obj)
      }
    }
    for (let i in obj) {
      // Skip methods
      if (!obj.hasOwnProperty(i)) {
        continue
      }
      if (isObject(obj[i])) {
        let objIndex = objects.indexOf(obj[i])

        if (objIndex === -1) {
          // Object has not been processed; generate key and continue
          // (but don't generate key for arrays!)
          if (!isArray(obj[i])) {
            if (!obj[i][key]) { obj[i][key] = guid() }
            objects.push(obj[i])
          }
          // Process child properties
          // (note well: recursive call)
          convertToJsonHelper(obj[i], key, objects)
        } else {
          // Current object has already been processed;
          // replace it with existing reference
          obj[i] = objects[objIndex][key]
        }
      }
    }
    return obj
  }
  // As discussed above, the serializer needs to use some unique property name for
  // the IDs it generates. Here we use "@id" since presumably prepending the "@" to
  // the property name is adequate to ensure that it is unique. But any unique
  // property name can be used, as long as the same one is used by the serializer
  // and deserializer.
  //
  // Also note that we leave off the 3rd parameter in our call to
  // convertToJsonHelper since it will be initialized within that function if it
  // is not provided.
  return convertToJsonHelper(obj, '@id')
}

const convertToObject = function (json) {
  // Check if an object is an array
  const isObject = function (value) {
    return (typeof value === 'object')
  }
  // Iterate object properties and store all reference keys and references
  const getKeys = function (obj, key) {
    let keys = []
    for (let i in obj) {
      // Skip methods
      if (!obj.hasOwnProperty(i)) {
        continue
      }

      if (isObject(obj[i])) {
        keys = keys.concat(getKeys(obj[i], key))
      } else if (i === key) {
        keys.push({ key: obj[key], obj: obj })
      }
    }
    return keys
  }
  const convertToObjectHelper = function (json, key, keys) {
    // Store all reference keys and references to object map
    if (!keys) {
      keys = getKeys(json, key)

      let convertedKeys = {}

      for (let i = 0; i < keys.length; i++) {
        convertedKeys[keys[i].key] = keys[i].obj
      }
      keys = convertedKeys
    }
    let obj = json
    // Iterate all object properties and object children
    // recursively and replace references with real objects
    for (let j in obj) {
      // Skip methods
      if (!obj.hasOwnProperty(j)) {
        continue
      }
      if (isObject(obj[j])) {
        // Property is an object, so process its children
        // (note well: recursive call)
        convertToObjectHelper(obj[j], key, keys)
      } else if (j === key) {
        // Remove reference id
        delete obj[j]
      } else if (keys[obj[j]]) {
        // Replace reference with real object
        obj[j] = keys[obj[j]]
      }
    }
    return obj
  }
  // As discussed above, the serializer needs to use some unique property name for
  // the IDs it generates. Here we use "@id" since presumably prepending the "@" to
  // the property name is adequate to ensure that it is unique. But any unique
  // property name can be used, as long as the same one is used by the serializer
  // and deserializer.
  //
  // Also note that we leave off the 3rd parameter in our call to
  // convertToObjectHelper since it will be initialized within that function if it
  // is not provided.
  return convertToObjectHelper(json, '@id')
}

// http请求拦截器
service.interceptors.request.use(
  config => {
    if (config.method.toLowerCase() === 'post') { config.data = convertToJson(config.data) }
    return config
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    let res = response.data
    let logined = window.localStorage.getItem('logined')
    if (res) {
      // 如果返回内容指明需要执行login操作
      if (res.login && res.login === true && (!logined)) {
        window.localStorage.setItem('logined', 'true')
        // 这里用reload刷新了整个页面，可能会导致页面内容变化，最好能直接弹出登录窗口，登录后可以继续执行操作
        location.reload()
      } else {
        convertToObject(response.data)
      }
    }
    return res

    // return res

    // if (res.code !== 200) {
    //   console.log('err')
    // } else {
    //   return res
    // }
  },
  error => {
    console.log('err' + error) // for debug
  }
)

export default service
