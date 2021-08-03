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
export const getCollections = () => axios.get(`${BASE_URL}/user/collections`);
//收藏商品
export const reqCollectBook = (bookId) => axios.get(`${BASE_URL}/user/collect?action=add`, {params:{bookId}});
//购买商品
export const reqToBuy = (bookId) => axios.get(`${BASE_URL}/user/collect?action=buy`, {params:{bookId}});
export const reqToBuyAll = () => axios.get(`${BASE_URL}/user/collect?action=buy`);
//删除收藏
export const reqRemoveCollection = (bookId) => axios.get(`${BASE_URL}/user/collect?action=remove`, {params:{bookId}});
export const reqRemoveAllCollections = () => axios.get(`${BASE_URL}/user/collect?action=remove`);

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


//获取图书列表
export const reqBooks = () => axios.get(`${BASE_URL}/books`);


//添加图书
export const addBook = (data) => axios.post(`${BASE_URL}/admin/dobook?action=add`, data, {
  headers:{
    "Content-Type": "multipart/form-data"
  }
});
//删除图书
export const deleteBook = (bookId) => axios.post(`${BASE_URL}/admin/dobook?action=delete`, Qs.stringify({bookId}));
//编辑图书
export const editBook = (data) => axios.post(`${BASE_URL}/admin/dobook?action=edit&bookId=`, data, {
  headers:{
    "Content-Type": "multipart/form-data"
  }
})

export const myAxios = axios;
// export const updateToken = token => token = token;
