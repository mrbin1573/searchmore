<template>
  <div id="searchmore-content" :class="searchResultShow == true ? 'on-search' : ''">
    <div class="searchmore-box">
      <!-- <div class="searchmore-logo-box">
        <span class="searchmore-name">杉木搜索</span>
      </div> -->

      <!-- 搜索和设置 控制位置变换 -->
      <div class="input-search-box">
        <!-- 搜索框 -->
        <div class="input-box">
          <input type="text" id="inputObj"
          class="searchmore-input-text"
          :class="{afterSearch:searchResultShow}"
          ref="inputbox"
          v-model="searchTxt"
          @keyup.enter="inputEnter"
          @input="getBaidu"
          @blur="inputLeave"
          @focus="inputFocus"
          @keyup.up="tipUp"
          @keyup.down="tipDown"
          placeholder="请输入搜索内容" >

          <!-- 搜索按钮 -->
          <label class="searchmore-submit">
            <div class="submit-name hover-bg" 
            v-for="(searchObj, objName) in browserArr" 
            v-if="searchObj.current"
            @click="searchNow(objName)">搜索{{searchObj.name}}</div>
            <!-- 其他搜索类型 -->
            <div class="more-search-opts" @mouseover="moreSearchShow = true" @mouseleave="moreSearchShow = false">
              <div class="more-trigger hover-bg">
                <i class="icon-xiala"></i>
              </div>
              <transition name="to-top">
                <div class="more-search-list" v-show="moreSearchShow">
                  <ul>
                    <li  class="ms-item hover-bg"
                    v-for="(searchObj, objName) in browserArr"
                    v-if="searchObj.active"
                    @click="searchNow(objName)">{{searchObj.name}}</li>
                  </ul>
                </div>
              </transition>
            </div>
          </label>

          <!-- 三根聚焦时的线 -->
          <div class="border-line line-top" :class="inputFocusBool == true ? 'focus' : ''"></div>
          <div class="border-line line-bottom" :class="inputFocusBool == true ? 'focus' : ''"></div>
          <div class="line-left" :class="inputFocusBool == true ? 'focus' : ''"></div>

          <!-- 输入框首选引擎 -->
          <div class="first-engine" @mouseover="firstEngineShow = true" @mouseleave="firstEngineShow = false">
            <!-- 输入框显示引擎图标 -->
            <div class="now-engine" v-if="currentEngine.current"  v-for="(currentEngine, index) in browserArr">
              <i :class="currentEngine.icon" :style="{color:currentEngine.iconColor}"></i>
            </div>
            <transition name="to-top">
              <ul class="f-list-box" v-show="firstEngineShow" :style="{background: searchResultShow == true ? '#fff':''}">
                <!-- <div class="tip">切换首选搜索引擎</div> -->
                <li class="f-item" v-for="(currentEngine, index) in browserArr" @click="chooseThisEngineAnCurrent(index)" :class="{active:currentEngine.current}">
                  <i :class="currentEngine.icon" :style="{color:currentEngine.iconColor}"></i><span class="f-engine-name">{{currentEngine.name}}</span>
                </li>
              </ul>
            </transition>
          </div>

        </div>

        <!-- 搜索结果提示 -->
        <transition name="to-top">
          <div class="search-tip" v-show="searchTipObj.show">
            <ul>
              <li class="tip-item" :class="{active:index == searchTipObj.nowIndex}" v-for="(tipItem, index) in searchTipObj.data" @click="selectTip(index)"><span>{{tipItem}}</span></li>
            </ul>
          </div>
        </transition>

        <!-- 设置页面 -->
        <!-- <div class="set-panel"> -->
        <div class="set-panel" 
        @mouseover="setPanelShow = true" 
        @mouseleave="setPanelShow = false">
          
          <!-- 设置齿轮图标 -->
          <div class="set-icon">
            <i class="icon-setting"></i>
          </div>

          <!-- 设置 浏览器 二维码 -->
          <transition name="to-top">
            <div class="set-box blur-bg-effect" 
            v-show="setPanelShow"
            >
              
              <!-- 统计，说明 -->
              <h5 class="set-title">
                <span>
                  点击图标，启用/取消搜索引擎&emsp;&emsp;
                </span>
                <span id="busuanzi_container_site_pv">
                  搜索统计：<span id="busuanzi_value_site_pv"></span> 次
                </span>
              </h5>

              <!-- 搜索配置选项列表 -->
              <div class="browser-list-box clearfix" v-for="(bsItemObj, indexParent) in browserArr">
                <div class="switch-obj">
                  <input class="switch-btn" type="checkbox" :checked="bsItemObj.active" :name="bsItemObj.name" :id="bsItemObj.iconName">
                  <label :for="bsItemObj.iconName" @click="selectObj(indexParent)" class="switch-btn-label"></label>
                </div>
                <div class="obj-name">
                  <i :class="bsItemObj.iconName"></i>&nbsp;
                  <span>{{bsItemObj.name}}：</span>
                </div>
                <div class="browser-item" 
                v-for="(bsItem, indexChild) in bsItemObj.data"  
                :class="{txtUnavtive: !bsItem.active}" 
                @click="switchBrowser(indexParent, indexChild)">
                  <h4 class="browser-name">{{bsItem.name}}</h4>
                </div>
                <!-- 手动添加按钮 -->
                <div class="browser-item active" @click="addBrowser()">
                  <div class="browser-icon" style="background:#4e4e4e;">
                    <i class="icon-add"></i>
                  </div>
                  <h4 class="browser-name">添加</h4>
                </div>
              </div>

              <!-- 扫一扫 -->
              <!-- <div class="erwema-box">
                <div class="erweima-title">只要扫一下其中一个二维码，你的钱包就会少两块钱</div>
                <div class="erweima-content">
                  <div class="erweima-img-box">
                    <img class="erweima-img" src="http://hackbinimg.luokangyuan.com/getmoney/getmoneyqq.png" alt="">
                    <span>QQ</span>
                  </div>
                  <div class="erweima-img-box">
                    <img class="erweima-img" src="http://hackbinimg.luokangyuan.com/getmoney/getmoneywx.png" alt="">
                    <span>微信</span>
                  </div>
                  <div class="erweima-img-box">
                    <img class="erweima-img" src="http://hackbinimg.luokangyuan.com/getmoney/getmoneyzfb.png" alt="">
                    <span>支付宝</span>
                  </div>
                </div>
              </div> -->

              <!-- <div class="blur-bg" :style="{backgroundImage: 'url(' +bgObj.src+ ')'}"></div> -->
            </div>
          </transition>
        </div>
      </div>

            
      <!-- 搜索结果 -->
      <div class="search-result-content" v-show="searchResultShow">
        <!-- 头部 -->
        <div class="result-header">
        </div>

        <!-- 结果iframe -->
        <div class="result-iframe-content" v-for="resultObj in browserArr" v-if="resultObj.current">
          <transition-group name="to-bottom" tag="div">
            <iframe class="iframe-item" 
            v-for="resultItemFrame in resultObj.data"
            v-show="resultItemFrame.current == true && resultItemFrame.active == true"
            :key="resultItemFrame.icon"
            :style="resultItemFrame.resultIframeStyle"
            :src="resultItemFrame.resultSrc"
            frameborder="0"></iframe>
          </transition-group>
        </div>
      </div>
    </div>

    <!-- 搜索结果页面logo-->
    <transition name="to-bottom">
      <a href="javascript:window.location.reload()" title="返回主页" class="result-logo" v-show="searchResultShow">
        <img class="r-logo-img" src="http://hackbinimg.luokangyuan.com/searchmore/logored.gif" alt="" srcset="">
        <span>杉木搜索</span>
      </a>
    </transition>
    <!-- 搜索结果页面浏览器切换按钮 -->
    <transition name="to-bottom">
      <div class="hd-broser-list" v-show="searchResultShow" v-for="(bsItemArr, parentObjName) in browserArr" v-if="bsItemArr.current">
        <transition-group name="to-bottom" tag="div">
          <div class="item"
          v-for="(bsItem, indexChild) in bsItemArr.data" 
          :key="bsItem.name"
          :class="bsItem.current == true ? 'active hover-bg' : ''"
          v-if="bsItem.icon != 'icon-add' && bsItem.active == true"
          @click="changeBrowser(parentObjName, indexChild)">
            <i :class="bsItem.icon"></i>
            {{bsItem.name}}
          </div>
        </transition-group>
      </div>
    </transition>


    <!-- 背景 -->
    <div class="sm-bg" v-show="!searchResultShow">
      <video :class="{focus:bgBlur}" v-if="bgObj.type == 'video'" class="bg"  muted autoplay loop :src="bgObj.src"></video>
      <img :class="{focus:bgBlur}" v-if="bgObj.type == 'image'" class="bg" :src="bgObj.src"/>
    </div>

    <!-- 弹窗 -->
    <AlertBox :alertObj="alertObj"></AlertBox>

    <!-- 书签 -->
    <div class="book-mark">
      <div class="bm-content">
        <div class="triggrt">
          <i class="icon-you"></i>
        </div>
        <div class="bm-list-box">
          <ul class="bm-list">
            <li class="bm-item">
              <i class="icon-ditu"></i>
              <span>工作</span>
            </li>
            <li class="bm-item">
              <i class="icon-ditu"></i>
              学习
            </li>
            <li class="bm-item">
              <i class="icon-ditu"></i>
              生活
            </li>
          </ul>
        </div>

        <div class="sub-list-content">
          <div class="sub-list-box">

            <div class="bm-bg-box">
              <div class="bm-bg" :style="{backgroundImage: 'url(' +bgObj.src+ ')'}"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="bm-bg-box">
        <div class="bm-bg" :style="{backgroundImage: 'url(' +bgObj.src+ ')'}"></div>
      </div>
    </div>

  </div>
</template>

<script>
import AlertBox from '../src/components/alert.vue'
export default {
  name: 'App',
  components: {
    AlertBox
  },
  data () {
    return {
      // 背景
      bgObj: {
        type: 'image',
        src: ''
        // src: 'http://hackbinimg.luokangyuan.com/searchmore/bg1.jpg'
      },
      // 输入框聚焦
      inputFocusBool: false,
      // 控制设置面板显示
      setPanelShow: false,
      // 搜索结果展示
      searchResultShow: false,
      // 输入框下拉关键词
      searchTipObj: {
        show: false,
        data: [],
        nowIndex: -1,
      },
      // 背景模糊
      bgBlur: false,
      // 输入框设置首选浏览器展示
      firstEngineShow: false,
      // 输入提交按钮，更多搜索选项展示
      moreSearchShow: false,
      // 搜索引擎列表
      browserArr: {
        web:{
          active: true, // 是否可用
          current: true, // 是否当前
          name:'网页',
          iconName:'icon-wangluo',
          data:[
            {
              name: "百度",
              browserUrl: 'https://www.baidu.com/s?wd=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-baidu",
              use: true, // 是否首选项
              active: true, // 是否激活可用状态
              current: true, // 是否为当前选中显示搜索结果页面
              iconColor: "#3385ff", // 图标颜色
              resultIframeStyle: { //搜索结果的单独样式
              },
            },
            {
              name: "谷歌",
              browserUrl: 'https://google.suanfazu.com/search/?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-google",
              use: false,
              active: true,
              current: false,
              iconColor: "rgb(251, 188, 5)",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-53px',
                height:'calc(100% + 53px)'
              },
            },
            {
              name: "360",
              browserUrl: 'https://www.so.com/s?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-360",
              use: false,
              active: true,
              current: false,
              iconColor: "#1bc550",
              resultIframeStyle: {},
            },
            {
              name: "搜狗",
              browserUrl: 'https://www.sogou.com/web?query=',
              resultSrc:'',
              icon: "icon-sougou",
              bgColor: "background: #f84a19",
              use: false,
              active: true,
              current: false,
              iconColor: "#f84a19",
              resultIframeStyle: {},
            },
            {
              name: "必应",
              browserUrl: 'https://cn.bing.com/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              use: false,
              active: true,
              current: false,
              iconColor: "#007daa",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-35px',
                height:'calc(100% + 35px)'
              },
            }
          ]
        },
        image:{
          active: true,
          current: false,
          name:'图片',
          iconName:'icon-img',
          data:[
            {
              name: "百度",
              browserUrl: 'https://www.baidu.com/s?wd=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-baidu",
              use: true, // 是否首选项
              active: true, // 是否激活可用状态
              current: true, // 是否为当前选中显示搜索结果页面
              iconColor: "#3385ff", // 图标颜色
              resultIframeStyle: { //搜索结果的单独样式
              },
            },
            {
              name: "谷歌",
              browserUrl: 'https://google.suanfazu.com/search/?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-google",
              use: false,
              active: true,
              current: false,
              iconColor: "rgb(251, 188, 5)",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-53px',
                height:'calc(100% + 53px)'
              },
            },
            {
              name: "360",
              browserUrl: 'https://www.so.com/s?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-360",
              use: false,
              active: true,
              current: false,
              iconColor: "#1bc550",
              resultIframeStyle: {},
            },
            {
              name: "搜狗",
              browserUrl: 'https://www.sogou.com/web?query=',
              resultSrc:'',
              icon: "icon-sougou",
              bgColor: "background: #f84a19",
              use: false,
              active: true,
              current: false,
              iconColor: "#f84a19",
              resultIframeStyle: {},
            },
            {
              name: "必应",
              browserUrl: 'https://cn.bing.com/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              use: false,
              active: true,
              current: false,
              iconColor: "#007daa",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-35px',
                height:'calc(100% + 35px)'
              },
            }
          ]
        },
        music:{
          active: false,
          current: false,
          name:'音乐',
          iconName:'icon-music',
          data:[
            {
              name: "百度",
              browserUrl: 'https://www.baidu.com/s?wd=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-baidu",
              use: true, // 是否首选项
              active: true, // 是否激活可用状态
              current: true, // 是否为当前选中显示搜索结果页面
              iconColor: "#3385ff", // 图标颜色
              resultIframeStyle: { //搜索结果的单独样式
              },
            },
            {
              name: "谷歌",
              browserUrl: 'https://google.suanfazu.com/search/?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-google",
              use: false,
              active: true,
              current: false,
              iconColor: "rgb(251, 188, 5)",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-53px',
                height:'calc(100% + 53px)'
              },
            },
            {
              name: "360",
              browserUrl: 'https://www.so.com/s?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-360",
              use: false,
              active: true,
              current: false,
              iconColor: "#1bc550",
              resultIframeStyle: {},
            },
            {
              name: "搜狗",
              browserUrl: 'https://www.sogou.com/web?query=',
              resultSrc:'',
              icon: "icon-sougou",
              bgColor: "background: #f84a19",
              use: false,
              active: true,
              current: false,
              iconColor: "#f84a19",
              resultIframeStyle: {},
            },
            {
              name: "必应",
              browserUrl: 'https://cn.bing.com/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              use: false,
              active: true,
              current: false,
              iconColor: "#007daa",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-35px',
                height:'calc(100% + 35px)'
              },
            }
          ]
        },
        video:{
          active: false,
          current: false,
          name:'影视',
          iconName:'icon-shipin',
          data:[
            {
              name: "百度",
              browserUrl: 'https://www.baidu.com/s?wd=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-baidu",
              use: true, // 是否首选项
              active: true, // 是否激活可用状态
              current: true, // 是否为当前选中显示搜索结果页面
              iconColor: "#3385ff", // 图标颜色
              resultIframeStyle: { //搜索结果的单独样式
              },
            },
            {
              name: "谷歌",
              browserUrl: 'https://google.suanfazu.com/search/?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-google",
              use: false,
              active: true,
              current: false,
              iconColor: "rgb(251, 188, 5)",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-53px',
                height:'calc(100% + 53px)'
              },
            },
            {
              name: "360",
              browserUrl: 'https://www.so.com/s?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-360",
              use: false,
              active: true,
              current: false,
              iconColor: "#1bc550",
              resultIframeStyle: {},
            },
            {
              name: "搜狗",
              browserUrl: 'https://www.sogou.com/web?query=',
              resultSrc:'',
              icon: "icon-sougou",
              bgColor: "background: #f84a19",
              use: false,
              active: true,
              current: false,
              iconColor: "#f84a19",
              resultIframeStyle: {},
            },
            {
              name: "必应",
              browserUrl: 'https://cn.bing.com/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              use: false,
              active: true,
              current: false,
              iconColor: "#007daa",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-35px',
                height:'calc(100% + 35px)'
              },
            }
          ]
        },
        map:{
          active: false,
          current: false,
          name:'地图',
          iconName:'icon-ditu',
          data:[
            {
              name: "百度",
              browserUrl: 'https://www.baidu.com/s?wd=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-baidu",
              use: true, // 是否首选项
              active: true, // 是否激活可用状态
              current: true, // 是否为当前选中显示搜索结果页面
              iconColor: "#3385ff", // 图标颜色
              resultIframeStyle: { //搜索结果的单独样式
              },
            },
            {
              name: "谷歌",
              browserUrl: 'https://google.suanfazu.com/search/?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-google",
              use: false,
              active: true,
              current: false,
              iconColor: "rgb(251, 188, 5)",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-53px',
                height:'calc(100% + 53px)'
              },
            },
            {
              name: "360",
              browserUrl: 'https://www.so.com/s?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-360",
              use: false,
              active: true,
              current: false,
              iconColor: "#1bc550",
              resultIframeStyle: {},
            },
            {
              name: "搜狗",
              browserUrl: 'https://www.sogou.com/web?query=',
              resultSrc:'',
              icon: "icon-sougou",
              bgColor: "background: #f84a19",
              use: false,
              active: true,
              current: false,
              iconColor: "#f84a19",
              resultIframeStyle: {},
            },
            {
              name: "必应",
              browserUrl: 'https://cn.bing.com/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              use: false,
              active: true,
              current: false,
              iconColor: "#007daa",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-35px',
                height:'calc(100% + 35px)'
              },
            }
          ]
        },
        file:{
          active: false,
          current: false,
          name:'文档',
          iconName:'icon-wenxianjiansuoshenqing',
          data:[
            {
              name: "百度",
              browserUrl: 'https://www.baidu.com/s?wd=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-baidu",
              use: true, // 是否首选项
              active: true, // 是否激活可用状态
              current: true, // 是否为当前选中显示搜索结果页面
              iconColor: "#3385ff", // 图标颜色
              resultIframeStyle: { //搜索结果的单独样式
              },
            },
            {
              name: "谷歌",
              browserUrl: 'https://google.suanfazu.com/search/?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-google",
              use: false,
              active: true,
              current: false,
              iconColor: "rgb(251, 188, 5)",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-53px',
                height:'calc(100% + 53px)'
              },
            },
            {
              name: "360",
              browserUrl: 'https://www.so.com/s?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-360",
              use: false,
              active: true,
              current: false,
              iconColor: "#1bc550",
              resultIframeStyle: {},
            },
            {
              name: "搜狗",
              browserUrl: 'https://www.sogou.com/web?query=',
              resultSrc:'',
              icon: "icon-sougou",
              bgColor: "background: #f84a19",
              use: false,
              active: true,
              current: false,
              iconColor: "#f84a19",
              resultIframeStyle: {},
            },
            {
              name: "必应",
              browserUrl: 'https://cn.bing.com/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              use: false,
              active: true,
              current: false,
              iconColor: "#007daa",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-35px',
                height:'calc(100% + 35px)'
              },
            }
          ]
        },
        buy:{
          active: false,
          current: false,
          name:'购物',
          iconName:'icon-gouwu',
          data:[
            {
              name: "百度",
              browserUrl: 'https://www.baidu.com/s?wd=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-baidu",
              use: true, // 是否首选项
              active: true, // 是否激活可用状态
              current: true, // 是否为当前选中显示搜索结果页面
              iconColor: "#3385ff", // 图标颜色
              resultIframeStyle: { //搜索结果的单独样式
              },
            },
            {
              name: "谷歌",
              browserUrl: 'https://google.suanfazu.com/search/?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-google",
              use: false,
              active: true,
              current: false,
              iconColor: "rgb(251, 188, 5)",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-53px',
                height:'calc(100% + 53px)'
              },
            },
            {
              name: "360",
              browserUrl: 'https://www.so.com/s?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-360",
              use: false,
              active: true,
              current: false,
              iconColor: "#1bc550",
              resultIframeStyle: {},
            },
            {
              name: "搜狗",
              browserUrl: 'https://www.sogou.com/web?query=',
              resultSrc:'',
              icon: "icon-sougou",
              bgColor: "background: #f84a19",
              use: false,
              active: true,
              current: false,
              iconColor: "#f84a19",
              resultIframeStyle: {},
            },
            {
              name: "必应",
              browserUrl: 'https://cn.bing.com/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              use: false,
              active: true,
              current: false,
              iconColor: "#007daa",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-35px',
                height:'calc(100% + 35px)'
              },
            },
            {
              name: "必应",
              browserUrl: 'https://cn.bing.com/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              use: false,
              active: true,
              current: false,
              iconColor: "#007daa",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-35px',
                height:'calc(100% + 35px)'
              },
            },
            {
              name: "必应",
              browserUrl: 'https://cn.bing.com/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              use: false,
              active: true,
              current: false,
              iconColor: "#007daa",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-35px',
                height:'calc(100% + 35px)'
              },
            },
            {
              name: "必应",
              browserUrl: 'https://cn.bing.com/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              use: false,
              active: true,
              current: false,
              iconColor: "#007daa",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-35px',
                height:'calc(100% + 35px)'
              },
            },
            {
              name: "必应",
              browserUrl: 'https://cn.bing.com/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              use: false,
              active: true,
              current: false,
              iconColor: "#007daa",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-35px',
                height:'calc(100% + 35px)'
              },
            },
          ]
        },
      },
      // 搜索输入框值
      searchTxt: '',
      // 弹窗显示
      alertObj: {
        show: false,
        text: "提示文字",
        textEn: 'tips word'
      },
    }
  },
  methods: {
    // 搜索事件
    searchNow (objName) {
      var searchTxt = this.searchTxt; // 输入框文本
      this.$refs.inputbox.blur(); // 让输入框失去焦点
      if (searchTxt != '') {
        this.searchResultShow = true; // 展示搜索结果页面
        var brObj =  this.browserArr; // 所有内置搜索对象
        var currentObj = brObj[objName]; // 当前搜索对象数组
        for (let brObjItemName in brObj) {
          // 非当前项目
          // if (objName != brObjItemName) {
          //   currentObj.current = false;
          // } 
          // // 当前项目
          // else {
          //   currentObj.current = true;
          // }
          for (let j=0; j<currentObj.data.length; j++) {
            if(currentObj.data[j].current == true) {
              currentObj.data[j].resultSrc = currentObj.data[j].browserUrl + this.searchTxt;
            }
          }
        }
        
      } else {
        this.alertObj.show = true;
        this.alertObj.text = "请输入搜索内容";
        this.alertObj.textEn = "no empty, ojbk?";
      }
    },

    // 输入框enter
    inputEnter () {
      this.searchTipObj.show = false;
      this.searchNow();
    },

    // 聚焦输入框
    inputFocus () {
      // 未展现搜索结果时，改变背景图样式
      if(this.searchResultShow == false){
        this.bgBlur = true;
      }
      this.inputFocusBool = true;
      if(this.searchTipObj.data.length > 0) {
        this.searchTipObj.show = true;
      }
    },
 
    // 输入框失去焦点
    inputLeave () {
      this.searchTipObj.show = false;
      this.bgBlur = false;
      // if(this.searchTipObj.data.length > 0) {
        this.inputFocusBool = false;
      // }
    },

    // 切换搜索引擎
    changeBrowser (parentObjName, indexChild) {
      var brArr = this.browserArr[parentObjName].data;
      for ( var i=0; i<brArr.length; i++) {
        if (indexChild == i) {
          brArr[i].current = true;
        } else {
          brArr[i].current = false;
        };
      }
      this.searchNow (parentObjName);
    },

    // 添加搜索引擎
    addBrowser() {
      this.alertObj.show = true;
      this.alertObj.text = "明日开发";
      this.alertObj.textEn = "too young too simple";
    },
    // 开启、关闭搜索引擎
    switchBrowser (indexParent, indexChild) {
      this.browserArr[indexParent].data[indexChild].active = !this.browserArr[indexParent].data[indexChild].active;
    },

    // 百度提示
    getBaidu () {
      if (this.searchTxt != "") {
        this.$http.jsonp('https://sp0.baidu.com/5a1Fazu8AA54nxGko9WTAnF6hhy/su',
        {
          params:{wd:this.searchTxt},
          jsonp:'cb'
        })
        .then(function(res){
          if(res.data.s.length > 0) {
            this.searchTipObj.data = res.data.s;
            this.searchTipObj.show = true;
            this.searchTipObj.nowIndex = -1;
          } else {
            this.searchTipObj.show = false;
          }
        },function(res){
          console.log(res);
        });
      } else {
        this.searchTipObj.data = [];
      }
    },

    // 选中百度提示
    selectTip (index) {
      this.searchTxt = this.searchTipObj.data[index];
      this.searchNow();
      this.searchTipObj.show = false;
      this.searchTipObj.nowIndex = index;
    },


    // 键盘上
    tipUp () {
      this.searchTipObj.nowIndex--;
      if(this.searchTipObj.nowIndex <= -1) {
        this.searchTipObj.nowIndex = this.searchTipObj.data.length - 1;
      }
      this.searchTxt = this.searchTipObj.data[this.searchTipObj.nowIndex];
    },

    //键盘下
    tipDown () {
      this.searchTipObj.nowIndex++;
      if(this.searchTipObj.nowIndex >= this.searchTipObj.data.length) {
        this.searchTipObj.nowIndex = 0;
      }
      this.searchTxt = this.searchTipObj.data[this.searchTipObj.nowIndex];
    },

    // 输入框 切换首选搜索引擎
    chooseThisEngineAnCurrent (index) {
      for(let i = 0; i < this.browserArr.length; i++) {
        if(i == index) {
          this.browserArr[index].current = true;
        } else {
          this.browserArr[i].current = false;
        }
      }
    },

    // 激活、取消大类
    selectObj (indexParent) {
      this.browserArr[indexParent].active = !this.browserArr[indexParent].active;
    }
  },
  // 判断移动端
  beforeCreate () {
    if ((navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))) {
      alert("检测到你的手机太辣鸡，正在跳转到百度\n请用电脑使用本工具！");
      window.location.href="https://www.baidu.com";
    }

    // 必应壁纸
    this.$http.jsonp('http://bing.ioliu.cn/v1',
      {
        // params:{
        //   d: 1,
        //   w: 1920,
        //   h: 1080,
        //   r: 20
        // },
        jsonp:'callback'
      })
      .then(function(res){
        var bgSrc = res.data.data.url;
        var bgType = bgSrc.split('.')[3];
        if(bgType == 'jpg' || bgType == 'png' || bgType == 'gif'){
          this.bgObj.type = "image";
        } else {
          this.bgObj.type = "video";
        }
        this.bgObj.src = bgSrc;
      },function(res){
        console.log('失败')
        console.log(res);
      });
  },
  watch: {
    alertObj: {
　　　　handler(newValue, oldValue) {
          var a = setTimeout(function () {
            oldValue.show = false;
          }, 1200)
          if(newValue.show == false){
            this.bgBlur = false;
            clearTimeout(a);
          } else {
            this.bgBlur = true;
          }
　　　　},
　　　　deep: true
　　}
  }


}
</script>

<style lang="less">
@import 'assets/css/icon.css';
@import 'assets/css/common.less';
#searchmore-content{
  // display:none;
  position: relative;
  .searchmore-box{
    position: fixed;
    z-index:2;
    top:0px;
    left:0px;
    width: 100%;
    height: 100%;
    .input-search-box{
      position: absolute;
      z-index: 50;
      transform: translate(50%, 40vh);
      margin-left: -320px;
      width: 100%;
      height: 40px;
      .input-box{
        position: absolute;
        z-index: 3;
        width: 640px;
        vertical-align: middle;
        .line-left{
          position: absolute;
          z-index: 100;
          top:0;
          left:0;
          height: 100%;
          width:1px;
          background:#de1a1a;
        }
        .border-line{
          position: absolute;
          z-index: 100;
          background:@main-color;
          width:0%;
          height:1px;
          transition: @animateTime;
          &.line-top{
            left:0;
            top:0;
          }
          &.line-bottom{
            right:0;
            bottom:0px;
            transform: rotate(180deg);
          }
          &.focus{
            width:100%;
          }
        }
        .searchmore-input-text{
          position: relative;
          vertical-align: middle;
          border: none;
          width: 520px;
          height: 40px;
          background: rgba(255, 255, 255, .5);
          box-shadow: 0 0 30px rgb(255, 255, 255) inset;
          line-height: 40px;
          padding: 0 40px 0 30px;
          transition:@animateTime;
          font-size: 16px;
          float: left;
          outline: none;
          &.afterSearch{
            border-top:1px solid #d6d6d6;
            border-bottom:1px solid #d6d6d6;
          }
        }
        .searchmore-submit{
          position: relative;
          align-items: center;
          width: 120px;
          height: 40px;
          line-height: 40px;
          text-align: center;
          color: #fff;
          white-space: nowrap;
          background: @main-color;
          user-select: none;
          float: left;
          cursor: pointer;
          .submit-name{
            flex-shrink: 0;
            width: 100%;
          }
          .more-search-opts{
            position: absolute;
            top:40px;
            flex-shrink: 0;
            .more-trigger{
              position: absolute;
              font-size: 12px;
              margin-top: -40px;
              margin-left: 100px;
              width: 20px;
              height: 40px;
              background: @main-color;
              border-left: 1px solid #e03e3e;
              >i{
                display:inline-block;
                transition: @animateTime;
                transform: scale(.8);
              }
            }
            .more-search-list{
              width:120px;
              box-shadow: @box-shadow;
              .ms-item{
                list-style: none;
                background: @main-color;
              }
            }
            &:hover{
              .more-trigger{
                background: #cc1212;
                >i{
                  transform: translateY(3px) scale(.8);
                }
              }
            }
          }
        }
        .first-engine{
          position: absolute;
          margin-top: 40px;
          cursor: pointer;
          .now-engine{
            position: absolute;
            top: -40px;
            left: 0px;
            padding-left: 10px;
            line-height:40px;
            width:30px;
            .icon-xiala{
              font-size: 12px;
            }
          }
          .f-list-box{
            background: rgba(255, 255, 255, 0.8);
            border-bottom: 1px solid @main-color;
            box-shadow: @box-shadow;
            width: 520px;
            .f-item{
              height: 40px;
              line-height: 40px;
              padding-left: 10px;
              list-style: none;
              .f-engine-name{
                margin-left: 5px;
              }
              &:hover{
                border-left: 1px solid @main-color;
                background-color: #f1f1f1;
                color: @main-color;
              }
              &.active{
                border-left: 1px solid @main-color;
                background-color: #f1f1f1;
                color: @main-color;
              }
            }
            .tip{
              padding: 2px 30px 2px;
              font-size: 14px;
              color: #dedede;
            }
          }
        }
      }
      .set-panel{
        position: absolute;
        margin-top: 40px;
        width: 640px;
        z-index:4;
        &:hover{
          .icon-setting{
            transform: rotate(180deg);
          }
        }
        .set-icon{
          position: absolute;
          width:20px;
          height:30px;
          top:-30px;
          right:130px;
          i{
            cursor: pointer;
            display:inline-block;
            transition: @animateTime;
            font-size: 20px;
          }
        }
        .set-box{
          position:relative;
          width:100%;
          max-height: 320px;
          overflow-y: auto;
          background:rgba(255,255, 255, 0.8);
          box-shadow: @box-shadow;
          padding: 20px 20px 2px;
          .browser-list-box{
            display:flex;
            align-items: center;
            flex-wrap: wrap;
            // min-height:40px;
            padding: 10px 0;
            // margin-top:5px;
            overflow-x: auto;
            &:not(:last-child){
              border-bottom: 1px dashed #969696;
            }
            // 开关
            .switch-obj{
              height:30px;
              line-height:30px;
              user-select: none;
              display: flex;
              align-items:center;
              .switch-btn{
                opacity: 0;
                &:checked+.switch-btn-label{
                  background:#11d8c7;
                  &::after{
                    left:23px;
                  }
                }
              }
              .switch-btn-label{
                position:absolute;
                cursor: pointer;
                display:inline-block;
                width: 45px;
                height:24px;
                border-radius:15px;
                background:#d4d4d4;
                transition: @animateTime;
                &::after{
                  position: absolute;
                  content: "";
                  transition: @animateTime;
                  display:inline-block;
                  left:2px;
                  width:20px;
                  height:20px;
                  margin-top: 2px;
                  border-radius:100%;
                  background:rgb(255, 255, 255);
                }
              }
            }
            .obj-name{
              margin-left: 40px;
              height:30px;
              line-height:30px;
              display: flex;
              align-items:center;
              justify-content:center;
            }
            .browser-item{
              display:flex;
              flex-shrink: 0;
              align-items:center;
              justify-content:center;
              user-select: none;
              cursor: pointer;
              height:30px;
              padding: 4px 8px;
              margin-right: 10px;
              border-radius: 15px;
              transition: @animateTime;
              letter-spacing: 2px;
              color: @main-txt-color;
              .browser-icon{
                display:flex;
                justify-content:center;
                align-items:center;
                border-radius: 50%;
                color: #fff;
                width: 18px;
                height: 18px;
                font-size: 12px;
                transition:@animateTime;
              }
              .browser-name{
                margin-left: 4px;
              }
              &.txtUnavtive:hover{
                background: #818181;
              }
              &:hover{
                background: @main-color;
                .browser-name{
                  color: #fff !important;
                }
              }
            }
          }
          .erwema-box{
            margin-top:10px;
            border-top: 1px solid @main-color;
            padding: 10px;
            .erweima-content{
              margin-top:10px;
              display: flex;
              justify-content: center;
              .erweima-img-box{
                display: flex;
                flex-direction: column;
                align-items: center;
                font-size:14px;
                cursor: pointer;
                .erweima-img{
                  width: 100px;
                  height:100px;
                  margin: 0 10px;
                }
              }
            }
          }
          .set-title{
            text-align: center;
            color: @main-txt-color;
            font-size: 12px;
            margin-bottom: 10px;
          }
          .erweima-title{
            text-align: center;
            color: @main-txt-color;
            font-size: 14px;
          }
        }
        .blur-bg{
          position: fixed;
          top:0;
          left:0;
          margin-left: -50%;
          width:100%;
          height:100%;
          background-attachment: fixed;
          background-repeat: no-repeat;
          background-position: center;
          background-color: #fff;
          background-size: 100%;
          transform: scale(1.1);
          // filter: blur(30px);
        }
      }
      .search-tip{
        position: absolute;
        width: 520px;
        margin-top: 40px;
        background: rgba(255, 255, 255, 0.8);
        box-shadow: @box-shadow;
        border-bottom: 1px solid @main-color;
        .tip-item{
          list-style: none;
          padding: 0 10px;
          height: 30px;
          line-height: 30px;
          // transition: @animateTime;
          cursor: pointer;
          span{
            display: inline-block;
            transition: @animateTime;
          }
          &:hover{
            border-left: 1px solid @main-color;
            background-color: #f1f1f1;
            color: @main-color;
          }
          &.active{
            border-left: 1px solid @main-color;
            background-color: #f1f1f1;
            color: @main-color;
          }
        }
      }
    }
    .search-result-content{
      position: fixed;
      top:0;
      left:0;
      width:100%;
      height: 100%;
      .result-header{
        position: relative;
        z-index: 10;
        display: flex;
        align-items: center;
        width:100%;
        background: #fff;
        height: 50px;
        
      }
      .result-iframe-content{
        position: fixed;
        z-index: 9;
        top: 0;
        left:0;
        width: 100%;
        height: 100%;
        background: #fff;
        .iframe-item{
          position: absolute;
          width: 100%;
          height:100%;
        }
      }
    }
  }

  .result-logo{
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: fixed;
    z-index: 60;
    left: 0px;
    top: 0px;
    padding: 10px 12px;
    width:120px;
    height: 50px;
    font-size: 17px;
    font-weight: bold;
    text-decoration: none;
    color: @main-color;
    .r-logo-img{
      height: 30px;
    }
  }
  .hd-broser-list > div{
    position: fixed;
    z-index: 60;
    height: 50px;
    display: flex;
    flex-direction: row;
    align-items: center;
    width: calc(~'100% - 780px');
    left: 780px;
    .item{
      // float: left;
      margin-right: 15px;
      padding: 0 15px;
      height: 36px;
      line-height: 36px;
      font-size: 18px;
      font-weight: bold;
      border-radius: 40px;
      border:1px solid @main-color;
      color: @main-color;
      cursor: pointer;
      user-select: none;
      transition: @animateTime;
      &.active{
        color: #fff;
        background: @main-color;
      }
      &:hover{
        background: #f7f7f7;
      }
    }
  }

  .sm-bg{
    position: fixed;
    top:0px;
    left:0px;
    z-index: 1;
    display: flex;
    justify-content:center;
    align-items:center;
    width: 100%;
    height: 100%;
    min-width: 1300px;
    background: #ffffff;
    .bg{
      transition: @animateTime;
      // transform: scale(1.1);
      &.focus{
        filter: brightness(60%);
        transform: scale(1.02);
      }
      width: 100%;
    }
  }
  &.on-search{
    .input-search-box{
      transition: @animateTime * 1.5;
      transform: translateY(5px) translateX(440px);
      .set-box,.search-tip{
        background: #fff !important;
        li:hover{
            background-color: #f1f1f1;
          }
      }
    }
  }
  .book-mark{
    position:fixed;
    top:0;
    left:0;
    // transform: translateX(-100%);
    transform: translateX(0%);
    z-index: 150;
    height: 100%;
    width: 100px;
    box-shadow: @box-shadow;
    transition: @animateTime;
    &:hover{
      transform: translateX(0%);
      .triggrt{
        transform: rotate(180deg);
        // margin-left: 40px;
        // justify-content: flex-start;
        background: rgba(255, 255, 255, 0.2) !important;
      }
    }
    .bm-content{
      position: absolute;
      width:100%;
      height:100%;
      z-index: 200;
      .triggrt{
        position: absolute;
        top: calc(~'50% - 25px');
        left: calc(~'100% - 25px');
        width: 50px;
        height: 50px;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        border-radius: 50%;
        color:#fff;
        font-weight:bold;
        background: @main-color;
        transition:@animateTime;
        transform-origin: 50% 50%;
      }
      .bm-list-box{
        width:100%;
        height:100%;
        .bm-list{
          height:100%;
          width:100%;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          .bm-item{
            position: relative;
            list-style-type: none;
            height:40px;
            line-height: 40px;
            width:100%;
            text-align: center;
            border-top: 1px solid rgba(0, 0, 0, 0.1);
            border-bottom: 1px solid rgba(0, 0, 0, 0.1);
            cursor: pointer;
            font-weight: bold;
            color:@main-txt-color;
            &:hover{
              background:rgba(255, 255, 255, .5);
            }
          }
        }
      }
      .sub-list-content{
        position: absolute;
        display: flex;
        align-items: center;
        top:0;
        left: 100px;
        width: 100vw;
        height:100%;
        .sub-list-box{
          margin-left: 40px;
          background:rgba(255, 255, 255, 0.616);
          width: 800px;
          height:500px;
          overflow: hidden;
          box-shadow: @box-shadow;
        }
      }
    }
    .bm-bg-box{
      position: relative;
      width:100%;
      height:100%;
      z-index: 100;
      overflow: hidden;
      .bm-bg{
        overflow: hidden;
        position: relative;
        margin-left: -50%;
        margin-top: -50%;
        width:200%;
        height:200%;
        background-attachment: fixed;
        background-repeat: no-repeat;
        background-position: center;
        background-color: #fff;
        background-size: 100%;
        // transform: scale(1.1);
        filter: blur(50px);
      }
    }
  }
}
</style>
