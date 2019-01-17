<template>
  <div id="searchmore-content" :class="searchResultShow == true ? 'on-search' : ''">
    <div class="searchmore-box">
      <!-- <div class="searchmore-logo-box">
        <span class="searchmore-name">杉木搜索</span>
        {{commonBookMarkShow}}
      </div> -->

      <!-- 搜索和设置 控制位置变换 -->
      <div class="input-search-box">
        <!-- 搜索框 -->
        <div class="input-box">
          <input type="text" id="inputObj"
          class="searchmore-input-text"
          :class="{afterSearch:searchResultShow}"
          ref="inputbox" 
          v-for="(searchObj, indexParent) in browserArr" 
          v-if="searchObj.current"
          v-model="searchTxt"
          @keyup.enter="inputEnter(indexParent)"
          @input="getBaidu"
          @blur="inputLeave"
          @focus="inputFocus"
          @keyup.up="tipUp"
          @keyup.down="tipDown"
          placeholder="请输入搜索内容" >

          <!-- 搜索按钮 -->
          <label class="searchmore-submit">
            <div class="submit-name hover-bg" 
            v-for="(searchObj, index) in browserArr" 
            v-if="searchObj.current"
            @click="searchNow(index)">搜索{{searchObj.name}}</div>
            <!-- 其他搜索类型 -->
            <div class="more-search-opts" @mouseover="moreSearchShow = true" @mouseleave="moreSearchShow = false">
              <div class="more-trigger hover-bg">
                <i class="icon-xiala"></i>
              </div>
              <transition name="to-top">
                <div class="more-search-list" v-show="moreSearchShow">
                  <ul>
                    <li  class="ms-item hover-bg"
                    v-for="(searchObj, index) in browserArr"
                    v-if="searchObj.active"
                    @click="searchNow(index)">{{searchObj.name}}</li>
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
          <!-- <div class="first-engine"  
          v-for="(currentEngineArr, indexParent) in browserArr"
          v-if="currentEngineArr.current"  
          @mouseover="firstEngineShow = true"
          @mouseleave="firstEngineShow = false">
            输入框显示引擎图标
            <div class="now-engine" v-for="currentEngine in currentEngineArr.data" v-show="currentEngine.current">
              <i :class="currentEngine.icon" :style="{color:currentEngine.iconColor}"></i>
            </div>
            <transition name="to-top">
              <ul class="f-list-box" v-show="firstEngineShow" :style="{background: searchResultShow == true ? '#fff':''}">
                <div class="tip">切换首选搜索引擎</div>
                <li class="f-item" v-for="(currentEngine, indexChild) in currentEngineArr.data" @click="chooseThisEngineAnCurrent(indexParent, indexChild)" :class="{active:currentEngine.current}">
                  <i :class="currentEngine.icon" :style="{color:currentEngine.iconColor}"></i><span class="f-engine-name">{{currentEngine.name}}</span>
                </li>
              </ul>
            </transition>
          </div> -->

        </div>

        <!-- 搜索结果 提示 -->
        <transition name="to-top">
          <div class="search-tip" 
          v-show="searchTipObj.show"
          v-for="(searchObj, indexParent) in browserArr" 
          v-if="searchObj.current"
          >
            <ul>
              <li class="tip-item" 
              :class="{active:indexChild == searchTipObj.nowIndex}" 
              v-for="(tipItem, indexChild) in searchTipObj.data" 
              @click="selectTip(indexParent, indexChild)">
                <span>{{tipItem}}</span>
              </li>
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
                  设置搜索类型&emsp;&emsp;
                </span>
                <span id="busuanzi_container_site_pv">
                  搜索统计：<span id="busuanzi_value_site_pv"></span> 次
                </span>
              </h5>

              <!-- 搜索配置选项 列表 -->
              <div class="browser-list-box clearfix" 
              v-for="(bsItemObj, indexParent) in browserArr"
              draggable="true">
                <div class="touch icon-tuodong"></div>
                <MySwitch 
                :switchChecked="bsItemObj.active" 
                :switchName="bsItemObj.name"
                :switchId="indexParent"
                :fun="selectobj(indexParent)"
                ></MySwitch>
                <!-- <div class="switch-obj">
                  <input class="switch-btn" type="checkbox" 
                  :switchChecked="bsItemObj.active" 
                  :switchName="bsItemObj.name" 
                  :switchId="bsItemObj.iconName">
                  <label :for="bsItemObj.iconName" @click="selectObj(indexParent)" class="switch-btn-label"></label>
                </div> -->
                <div class="obj-name">
                  <!-- <i :class="bsItemObj.iconName"></i>&nbsp; -->
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
                  <div class="browser-icon mid-center" style="background:#4e4e4e;">
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
                  
      <!-- 常用收藏书签 -->
      <transition name="to-top">
        <div 
        class="comom-book-mark" 
        v-for="(bmItem, indexParent) in bookMark" 
        v-if="bmItem.name == '常用'"
        v-show="commonBookMarkShow"
        >
          <BookMark :bookMarkArr="bmItem.data"></BookMark>
        </div>
      </transition>
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
    <transition name="opacityShow">
      <div class="book-mark" 
      @mouseover="subBookMarkShow = true" 
      @mouseleave="subBookMarkShow = false"
      v-show="this.$store.state.sideBookMarkShowState"
      >
        <div class="bm-content">
          <div class="triggrt">
            <i class="icon-you"></i>
          </div>
          <!-- 大类列表 -->
          <div class="bm-list-box">
            <ul class="bm-list">
              <li  
              draggable="true" 
              class="bm-item hover-bg" 
              v-for="(bmItem, index) in bookMark" 
              @mouseover="hoverBookMark(index)"
              :style="{order:bmItem.order}"
              >
                <div class="touch icon-tuodong"></div>
                <i :class="bmItem.icon"></i>
                <span class="menu-name">{{bmItem.name}}</span>
              </li>
            </ul>
          </div>

          <!-- 子类弹出框 -->
          <transition name="opacityShow">
            <div class="sub-list-content" v-show="subBookMarkShow">
              <div class="sub-list-box" :class="{whitebg: searchResultShow}">
                <div class="content-box" v-for="(bmItem, indexParent) in bookMark" v-show="bmItem.show">
                  <div class="sub-box-title">
                    {{bmItem.name}}
                  </div>
                  <div class="submenu-list-box">
                    <BookMark :bookMarkArr="bmItem.data"></BookMark>
                  </div>
                </div>
                <div class="bm-bg-box">
                  <div class="bm-bg" v-show="!searchResultShow" :style="{backgroundImage: 'url(' +bgObj.src+ ')'}"></div>
                </div>
              </div>
            </div>
          </transition>
        </div>
        <!-- <div class="bm-bg-box">
          <div class="bm-bg" :style="{backgroundImage: 'url(' +bgObj.src+ ')'}"></div>
        </div> -->
      </div>
    </transition>


    <!-- 下载壁纸 -->
    <a 
    class="down-wallpapper mid-center" 
    title="下载壁纸"
    :href="bgObj.src" 
    v-show="!searchResultShow"
    target="_blank" >
      <i class="icon-xiazai"></i>
    </a>

    <!-- 个人 -->
    <div class="personal"
      @mouseover="personnalShow = true"
      @mouseleave="personnalShow = false"
      v-show="!searchResultShow"
    >
      <div class="personal-head mid-center">
        <!-- <i class="icon-deng"></i> -->
        <img class="rukou" src="http://hackbinimg.luokangyuan.com/20180823130882/logowhite.gif" alt="" srcset="">
      </div>
      <transition name="to-top">
        <div class="personal-panel" v-show="personnalShow">
          <div class="uhead">
            <div class="head min-ccenter" style="backgroundImage:url(http://hackbinimg.luokangyuan.com/20190103110148/1.jpg)">
              <!-- <img class="head-img" :src="bgObj.src" alt="" srcset=""> -->
            </div>
            <div class="umsg">
              <h3>杉木河水流</h3>
            </div>
          </div>
          <div class="head-bg mid-center">
            <!-- <div class="bg" :style="{backgroundImage:'url(' +bgObj.src+ ')'}"></div> -->
            <div class="bg" style="backgroundImage:url(http://hackbinimg.luokangyuan.com/20190103110148/1.jpg)"></div>
          </div>
          <div class="u-settings">
            <ul>
              <li>
                <span>
                  <i class="icon-bookmark"></i>
                  <span class="name" title="是否在搜索栏底下显示常用收藏">常用收藏</span>
                </span>
                <MySwitch 
                  :switchChecked="true" 
                  :switchName="1"
                  :switchId="8888888888888888"
                  :fun="switchCommonBookMark"
                  ></MySwitch>
              </li>
              <li>
                <span>
                  <i class="icon-bookmarks"></i>
                  <span class="name" title="是否显示侧边收藏夹入口">侧边收藏</span>
                </span>
                <MySwitch 
                  :switchChecked="true" 
                  :switchName="1"
                  :switchId="9999999999999999999"
                  :fun="switchSideBookMark"
                  ></MySwitch>
              </li>
              <li class="cuosor_p hover-eff">
                <span>
                  <i class="icon-quill"></i>
                  <span class="name" title="是否显示侧边收藏夹入口">编辑收藏</span>
                </span>
              </li>
              <li class="cuosor_p hover-eff">
                <span>
                  <i class="icon-IE"></i>
                  <span class="name" title="是否显示侧边收藏夹入口">编辑搜索引擎</span>
                </span>
              </li>
            </ul>
          </div>
          <div class="send-data mid-center">
            <div class="btn mid-center hover-bg" @click="logOut">
              退出登录
            </div>
            <div class="btn mid-center save-data" @click="sendData">
              同步数据
            </div>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>
import AlertBox from '../src/components/alert.vue'
import BookMark from '../src/components/bookMark.vue'
import MySwitch from '../src/components/switch.vue'
export default {
  name: 'App',
  components: {
    AlertBox,
    BookMark,
    MySwitch
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

      // 书签子类展示
      subBookMarkShow: false,

      // 个人页面展示
      personnalShow: false,

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
      browserArr: [
        {
          active: true, // 是否可用
          current: true, // 是否当前
          order: 0,
          name:'网页',
          iconName:'icon-wangluo',
          data:[
            {
              name: "百度",
              browserUrl: 'https://www.baidu.com/s?wd=',
              resultSrc:'',// 搜索结果的链接
              order: 0,
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
        {
          active: true,
          current: false,
          name:'图片',
          iconName:'icon-img',
          data:[
            {
              name: "百度",
              browserUrl: 'https://image.baidu.com/search/index?tn=baiduimage&word=',
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
              browserUrl: 'http://image.so.com/i?q=',
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
              browserUrl: 'https://pic.sogou.com/pics?query=',
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
              browserUrl: 'https://cn.bing.com/images/search?q=',
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
        {
          active: false,
          current: false,
          name:'音乐',
          iconName:'icon-yinyue',
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
        {
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
        {
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
        {
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
        {
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
      ],
      // 书签
      bookMark: [
        {
          name:'学习',
          icon:'icon-zaixianxuexi',
          order:'1',
          show: true,
          data: [
            {
              name: 'csdn',
              icon: 'csdn',
              order: 1,
              bgColor:'',
              url:'http://w3more.cn/'
            },
            {
              name: 'vue',
              icon: 'V',
              order: 2,
              bgColor:'',
              url:'http://w3more.cn/'
            },
            {
              name: 'React',
              icon: 'React',
              order: 3,
              bgColor:'',
              url:'http://w3more.cn/'
            },
          ]
        },
        {
          name:'生活',
          icon:'icon-kafei',
          order:'2',
          show: false,
          data: [
            {
              name: '生活',
              icon: '生活',
              order: 1,
              bgColor:'',
              url:'http://w3more.cn/'
            },
            {
              name: '生活',
              icon: '生活',
              order: 2,
              bgColor:'',
              url:'http://w3more.cn/'
            },
            {
              name: '生活',
              icon: '生活',
              order: 3,
              bgColor:'',
              url:'http://w3more.cn/'
            },
          ]
        },
        {
          name:'工作',
          icon:'icon-gongzuo',
          order:'3',
          show: false,
          data: [
            {
              name: '工作',
              icon: '工作',
              order: 1,
              bgColor:'',
              url:'http://w3more.cn/'
            },
            {
              name: '工作',
              icon: '工作',
              order: 2,
              bgColor:'',
              url:'http://w3more.cn/'
            },
            {
              name: '工作',
              icon: '工作',
              order: 3,
              bgColor:'',
              url:'http://w3more.cn/'
            },
          ]
        },
        {
          name:'工作4',
          icon:'icon-gongzuo',
          order:'4',
          show: false,
          data: [
            {
              name: '工作',
              icon: '工作',
              order: 1,
              bgColor:'',
              url:'http://w3more.cn/'
            },
            {
              name: '工作',
              icon: '工作',
              order: 2,
              bgColor:'',
              url:'http://w3more.cn/'
            },
            {
              name: '工作',
              icon: '工作',
              order: 3,
              bgColor:'',
              url:'http://w3more.cn/'
            },
          ]
        },
        {
          name:'工作6',
          icon:'icon-gongzuo',
          order:'6',
          show: false,
          data: [
            {
              name: '工作',
              icon: '工作',
              bgColor:'',
              url:'http://w3more.cn/'
            },
            {
              name: '工作',
              icon: '工作',
              bgColor:'',
              url:'http://w3more.cn/'
            },
            {
              name: '工作',
              icon: '工作',
              bgColor:'',
              url:'http://w3more.cn/'
            },
          ]
        },
        {
          name:'工作5',
          icon:'icon-gongzuo',
          order:'5',
          show: false,
          data: [
            {
              name: '工作',
              icon: '工作',
              order: 1,
              bgColor:'',
              url:'http://w3more.cn/'
            },
            {
              name: '工作',
              icon: '工作',
              order: 2,
              bgColor:'',
              url:'http://w3more.cn/'
            },
            {
              name: '工作',
              icon: '工作',
              order: 3,
              bgColor:'',
              url:'http://w3more.cn/'
            },
          ]
        },
        {
          name:'常用',
          icon:'icon-gongzuo',
          order:'6',
          show: false,
          data: [
            {
              name: 'html教程',
              icon: 'html教程',
              order: 1,
              bgColor:'red',
              url:'http://w3more.cn/'
            },
            {
              name: '常用',
              icon: '常用',
              order: 2,
              bgColor:'blue',
              url:'http://w3more.cn/'
            },
            {
              name: '常用',
              icon: '常用',
              order: 3,
              bgColor:'green',
              url:'http://w3more.cn/'
            },
            {
              name: '常用',
              icon: '常用',
              order: 4,
              bgColor:'yellow',
              url:'http://w3more.cn/'
            },
            {
              name: '常用',
              icon: '常用',
              order: 5,
              bgColor:'purple',
              url:'http://w3more.cn/'
            },
            {
              name: '常用',
              icon: '常用',
              order: 6,
              bgColor:'black',
              url:'http://w3more.cn/'
            },
            {
              name: '常用',
              icon: '常用',
              order: 7,
              bgColor:'green',
              url:'http://w3more.cn/'
            },
            {
              name: '常用',
              icon: '常用',
              order: 8,
              bgColor:'blue',
              url:'http://w3more.cn/'
            },
            {
              name: '常用',
              icon: '常用',
              order: 9,
              bgColor:'red',
              url:'http://w3more.cn/'
            },
            {
              name: '常用',
              icon: '常用',
              order: 10,
              bgColor:'gray',
              url:'http://w3more.cn/'
            },
            {
              name: '常用',
              icon: '常用',
              order: 11,
              bgColor:'orange',
              url:'http://w3more.cn/'
            },
          ]
        },
      ],
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
    searchNow (index) {
      var searchTxt = this.searchTxt; // 输入框文本
      // this.$refs.inputbox.blur(); // 让输入框失去焦点
      if (searchTxt != '') {
        this.searchResultShow = true; // 展示搜索结果页面
        var brObj =  this.browserArr; // 所有内置搜索对象
        var currentObj = brObj[index]; // 当前搜索对象数组
        for (let i=0; i<brObj.length; i++) {
          if(i == index) {
            brObj[i].current = true;
            for (let j=0; j<currentObj.data.length; j++) {
              if(currentObj.data[j].current == true) {
                currentObj.data[j].resultSrc = currentObj.data[j].browserUrl + this.searchTxt;
              }
            }
          } else {
            brObj[i].current = false;
          }
        }
        
      } else {
        this.alertObj.show = true;
        this.alertObj.text = "请输入搜索内容";
        this.alertObj.textEn = "no empty, ojbk?";
      }
    },

    // 输入框enter
    inputEnter (index) {
      this.searchTipObj.show = false;
      this.searchNow(index);
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
    selectTip (indexParent, indexChild) {
      this.searchTxt = this.searchTipObj.data[indexChild];
      this.searchNow(indexParent);
      this.searchTipObj.show = false;
      this.searchTipObj.nowIndex = indexChild;
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
    chooseThisEngineAnCurrent (indexParent, indexChild) {
      var thisArr = this.browserArr[indexParent].data;
      for(let i = 0; i < thisArr.length; i++) {
        if(i == indexChild) {
          thisArr[i].current = true;
        } else {
          thisArr[i].current = false;
        }
      }
    },

    // 激活、取消大类
    selectobj (indexParent) {
      // this.browserArr[indexParent].active = !this.browserArr[indexParent].active;
    },

    // 鼠标覆盖变迁大类
    hoverBookMark (index) {
      for(let i=0; i<this.bookMark.length; i++) {
        if(i == index) {
          this.bookMark[i].show = true;
        } else {
          this.bookMark[i].show = false;
        }
      }
    },

    // 同步数据
    sendData () {
      this.$store.commit('changeBookMarkShow');
    },

    // 退出登录
    logOut () {

    },

    // 切换常用书签
    switchCommonBookMark() {
      this.$store.commit('changeBookMarkShow');
    },
    // 切换侧边书签
    switchSideBookMark () {
      this.$store.commit('changeSlideBookMarkShow');
    }
  },
  computed: {
    // 是否展示常用书签
    commonBookMarkShow: function () {
      return !(this.inputFocusBool || this.searchResultShow) && this.$store.state.commonBookMarkShowState;
      // return this.$store.state.commonBookMarkShowState;
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
        //   d: 4,
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
@import 'assets/css/style.css';
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
          padding: 0 40px 0 10px;
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
          background:rgba(255,255, 255, 0.9);
          box-shadow: @box-shadow;
          padding: 20px 20px 2px;
          .browser-list-box{
            display:flex;
            align-items: center;
            flex-wrap: wrap;
            padding: 10px 0;
            overflow-x: auto;
            user-select: none;
            &:not(:last-child){
              border-bottom: 1px dotted #969696;
            }
            .touch{
              height: 40px;
              line-height: 40px;
              cursor: move;
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
                background:#adadad;
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
              margin-left: 5px;
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
    .comom-book-mark{
      position: fixed;
      left: 50%;
      // top: calc(~'40vh + 60px');
      top: 50vh;
      margin-left: -320px - 4px; // 减去item的margin
      width: 648px + 4px + 13px; // 加上两边margin + 滚动条
      height: 216px;
      overflow-y: auto;
      // background: #dedede;
      display: flex;
      flex-wrap: wrap;
      .item{
        width: 100px;
        height:100px;
        margin-left: 8px;
        margin-bottom: 8px;
        background: rgba(0, 0, 0, 0.3);
        cursor: pointer;
        transition: @animateTime;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        transition: @animateTime;
        user-select: none;
        text-decoration: none;
        .icon{
          width: 55px;
          height: 55px;
          border-radius: 50%;
          background: rgba(0, 0, 0, 0.322);
          .icon-txt{
            color: #fff;
          }
        }
        .name{
          color:#fff;
          margin-top: 5px;
        }
        &:active{
          transform: scale(.9);
        }
        &:hover{
          background: rgba(48, 48, 48, 0.3);
          box-shadow: @box-shadow;
        }
        &:nth-child(6n + 1){
          margin-left: 0;
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
    transform: translateX(-99%);
    // transform: translateX(0%);
    z-index: 150;
    height: 100%;
    width: 100px;
    box-shadow: @box-shadow;
    transition: @animateTime;
    &:hover{
      transform: translateX(0%);
      .bm-content{
        .triggrt{
          transform: rotate(180deg);
          margin-left: 50px;
          // background: rgba(255, 255, 255, 0.2) !important;
        }
      }
    }
    .bm-content{
      position: absolute;
      width:100%;
      height:100%;
      z-index: 200;
      .triggrt{
        position: absolute;
        z-index: 110;
        // top: 40vh;
        top: calc(~"50% - 20px");
        left: calc(~'100% - 25px');
        display: flex;
        align-items: center;
        justify-content: flex-end;
        width: 40px;
        height: 40px;
        padding-right: 2px;
        cursor: pointer;
        border-radius: 50%;
        color:#fff;
        font-weight:bold;
        background: @main-color;
        transition:@animateTime;
        transform-origin: 50% 50%;
      }
      .bm-list-box{
        position: relative;
        z-index: 120;
        width:100%;
        height:100%;
        background: rgb(22, 22, 22);
        border-right: 2px solid @main-color;
        display: flex;
        align-items: center;
        .bm-list{
          height:100%;
          width:100%;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          .bm-item{
            position: relative;
            display: flex;
            list-style-type: none;
            height:50px;
            line-height: 50px;
            width:100%;
            text-align: center;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
            cursor: pointer;
            font-weight: bold;
            color:#fff;
            user-select: none;
            .touch,>i{
              height: 50px;
              line-height: 50px;
            }
            .touch{
              margin-right: 10px;
              color: rgb(209, 209, 209);
              &:hover{
                cursor: move;
              }
            }
            .menu-name{
              margin-left: 3px;
            }
            &:first-child{
              border-top: 1px solid rgba(255, 255, 255, 0.1);
            }
            &:hover{
              background:rgba(255, 255, 255, .5);
            }
          }
        }
      }
      .sub-list-content{
        position: fixed;
        z-index: 120;
        // background:rgb(51, 51, 51);
        // width: 800px;
        width: 70vw;
        height:80vh;
        top:50%;
        left: 100px;
        margin-top: -40vh;
        // width: 100vw;
        // height:100%;
        .sub-list-box{
          position: relative;
          margin-left: 40px;
          height:80vh;
          overflow: hidden;
          box-shadow: 5px 0 50px rgba(0, 0, 0, 0.3);
          background:rgba(255, 255, 255, 0.9);
          &.whitebg{
            background: rgb(255, 255, 255);
          }
          .content-box{
            position: absolute;
            z-index:100;
            display: flex;
            flex-direction: row;
            .sub-box-title{
              display: flex;
              align-items: center;
              justify-content: center;
              width:40px;
              height:80vh;
              background: @main-color;
              color: #fff;
              font-size: 20px;
              font-weight: bold;
              letter-spacing: 10px;
              writing-mode: vertical-lr;
            }
            .submenu-list-box{
              width:calc(~'100% - 40px');
              height:100%;
              padding: 20px;
              overflow: auto;
              height:80vh;
              .sub-menu-item{
                width:100px;
                height:100px;
                margin:2px;
                float: left;
                background: rgba(0, 0, 0, 0.3);
                cursor: pointer;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                transition: @animateTime;
                user-select: none;
                text-decoration: none;
                .icon{
                  width: 55px;
                  height: 55px;
                  border-radius: 50%;
                  background: rgba(0, 0, 0, 0.322);
                  .icon-txt{
                    color: #fff;
                  }
                }
                .name{
                  color:#fff;
                  margin-top: 5px;
                }
                &:active{
                  transform: scale(.9);
                }
                &:hover{
                  background: rgba(48, 48, 48, 0.3);
                  box-shadow: @box-shadow;
                }
              }
            }
          }
          .bm-bg-box{
            position: absolute;
            width: 100vw;
            height:100vh;
            z-index: 10;
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
              // background-color: #fff;
              background-size: 100%;
              // transform: scale(1.1);
              opacity: .8;
              filter: blur(50px) brightness(1.5);
            }
          }
        }
      }
    }
  }
  .down-wallpapper{
    position: fixed;
    z-index: 100;
    bottom: 10px;
    right:10px;
    width: 40px;
    height:40px;
    border-radius: 50%;
    opacity: 0.5;
    color: rgb(255, 255, 255);
    background: rgb(0, 0, 0);
    cursor: pointer;
    transition: @animateTime;
    &:hover{
      opacity: .8;
    }
  }
  .personal{
    position: fixed;
    top:10px;
    right:10px;
    z-index: 100;
    .personal-head{
      position: absolute;
      right: 0;
      width: 40px;
      height:40px;
      border-radius: 50%;
      color: rgb(255, 255, 255);
      background: rgba(0, 0, 0, 0.5);
      cursor: pointer; 
      cursor: poinrgba(0, 0, 0, 0.7);
      .rukou{
        width:20px;
      }
    }
    .personal-panel{
      position: relative;
      margin-top: 50px;
      width:350px;
      background: rgb(255, 255, 255);
      padding: 40px 40px 30px;
      box-shadow: @box-shadow;
      .uhead{
        position: relative;
        z-index: 20;
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        .head{
          width:150px;
          height:150px;
          border-radius: 100%;
          overflow: hidden;
          background-size: cover;
          background-position: center;
          background-repeat: no-repeat;
          border: 5px solid rgba(148, 148, 148, 0.719);
        }
        .umsg{
          margin-top: 10px;
          color: #fff;
          text-shadow: 0 0 5px rgba(0, 0, 0, 0.5);
        }
      }
      .head-bg{
        position: absolute;
        z-index: 10;
        top:0;
        left:0;
        width:100%;
        height: 250px;
        overflow: hidden;
        // background: #fff;
        .bg{
          width: 300px;
          height: 250PX;
          transform: scale(1.3);
          background-size: cover;
          background-position: center;
          background-repeat: no-repeat;
          filter: blur(25px) brightness(1.5);
        }
      }
      .u-settings{
        position: relative;
        margin-top: 30px;
        width:100%;
        >ul{
          width:100%;
          >li{
            width:100%;
            height:40px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            list-style: none;
            user-select: none;
            &:not(:last-child){
              border-bottom: 1px dotted #dedede;
            }
            // background: #dedede;
            .name{
              margin-left: 5px;
              color: @main-txt-color;
              font-weight: bold;
            }
            .des{
              margin-left: 3px;
              font-size: 12px;
              color: #acacac;
            }
          }
        }
      }
      .send-data{
        width:100%;
        margin-top: 10px;
        justify-content: space-around;
        .btn{
          width:100px;
          height:40px;
          border-radius: 20px;
          background: @main-color;
          color:#fff;
          font-weight: bold;
          user-select: none;
          box-shadow: @box-shadow;
          cursor: pointer;
          &.save-data{
            background: @main-green;
          }
          &:active{
            transform: scale(.95);
          }
        }
      }
    }
  }
}
</style>
