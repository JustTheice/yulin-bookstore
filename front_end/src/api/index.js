import axios from 'axios';
import Qs from 'qs';

import {BASE_URL} from './common';

let token = '';
// http request拦截器 添加一个请求拦截器
axios.interceptors.request.use(function (config) {
  // Do something before request is sent
  // debugger
  let token = window.localStorage.getItem("token")
  if (token) {
      config.headers.token = token;    //将token放到请求头发送给服务器
  }
  return config;
}, function (error) {
  // Do something with request error
  return Promise.reject(error);
});

//登陆
export const reqLogin = (data) => axios.post(`${BASE_URL}/user/login`, Qs.stringify(data));
//注册
export const reqRegister = (data) => axios.post(`${BASE_URL}/user/register`, Qs.stringify(data));
//获取收藏列表
export const getCollections = () => axios.get(`${BASE_URL}/user/collection`);
//收藏商品
export const reqCollectBook = (bookId) => axios.get(`${BASE_URL}/user/collect`, {params:{bookId}});
//退出登录
export const reqLogout = () => axios.get(`${BASE_URL}/user/logout`);
//自动登陆
export const reqAutoLogin = () => axios.get(`${BASE_URL}/user/autologin`);
//注销账号
export const reqCloseAccount = () => axios.get(`${BASE_URL}/user/close`);
//修改信息
export const reqChangeInfo = (data) => axios.post(`${BASE_URL}/user/change?action=base`, Qs.stringify(data));
//修改密码
export const reqChangePwd = (data) => axios.post(`${BASE_URL}/user/change?action=pwd`, Qs.stringify(data));
//充值
export const reqToPay = (payMoney) => axios.post(`${BASE_URL}/user/change?action=pay`, Qs.stringify({payMoney}));



// export const updateToken = token => token = token;
