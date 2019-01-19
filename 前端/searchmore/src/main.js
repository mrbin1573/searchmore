// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import  VueResource  from 'vue-resource';
import Vuex from 'vuex';
import axios from 'axios'

// 将 axios 添加的 Vue 原型上后就不需要再在每个需要使用它的页面引入了
Vue.prototype.$http = axios;

Vue.use(Vuex);
Vue.use(VueResource);
Vue.config.productionTip = false;
Vue.http.options.emulateJSON = true;

// 仓库
const store = new Vuex.Store({
  // 数据
  state: {
    // 常用书签展示
    commonBookMarkShowState: true,
    // 侧边书签入口显示
    sideBookMarkShowState: true,
  },
  // 突变，转变
  mutations: {
    // 切换常用书签显示
    changeBookMarkShow(state) {
      state.commonBookMarkShowState = !state.commonBookMarkShowState;
    },
    // 切换侧边书签
    changeSideBookMarkShow(state) {
      state.sideBookMarkShowState = !state.sideBookMarkShowState;
    }
  }
})
/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  components: { App },
  template: '<App/>'
})
