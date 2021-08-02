import { createApp } from 'vue'
import App from './App.vue'
import router from './router'


import './assets/css/reset.css'

const app = createApp(App);

// element-plus全局引入
import ElementPlus from 'element-plus';
import 'element-plus/lib/theme-chalk/index.css';
app.use(ElementPlus);

//图片懒加载
import lazyPlugin from 'vue3-lazy';
app.use(lazyPlugin, {
  loading: require('./assets/img/loading.gif'),
  error: require('./assets/img/loading.gif')
});

//axios配置

// element-plus 按需引入
// import elementPlusInit from './plugins/elementplus.js';
// elementPlusInit(app);

// import 'element-plus/packages/theme-chalk/src/base.scss';


app.use(router).mount('#app')
