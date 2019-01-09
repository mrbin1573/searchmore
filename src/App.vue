<template>
  <div id="searchmore-content" class="" ref="onSearch">
    <div class="searchmore-box">
      <!-- <div class="searchmore-logo-box">
        <span class="searchmore-name">杉木搜索</span>
      </div> -->

      <!-- 搜索和设置 控制位置变换 -->
      <div class="input-search-box">
        <!-- 搜索框 -->
        <div class="input-box">
          <input type="text" id="inputObj" 
          v-model="searchTxt"
          @keyup.enter="inputEnter"
          @keyup="getBaidu"
          @blur="inputLeave"
          @focus="inputFocus"
          @keyup.up="tipUp"
          @keyup.down="tipDown"
          placeholder="请输入搜索内容" class="searchmore-input-text">
          <label for="inputObj" class="searchmore-submit hover-bg" @click="searchNow">更多结果</label>
        </div>

        <!-- 搜索结果提示 -->
        <transition name="to-top">
          <div class="search-tip" v-show="searchTipShow">
            <ul>
              <li v-for="tipItem in searchTipArr" @click="selectTip($event)"><span>{{tipItem}}</span></li>
            </ul>
          </div>
        </transition>

        <!-- 设置页面 -->
        <div class="set-panel" @mouseover="showSetPanel" @mouseleave="hideSetPanel">
          
          <!-- 设置图标 -->
          <div class="set-icon">
            <i class="icon-setting"></i>
          </div>

          <!-- 设置浏览器 二维码 -->
          <transition name="to-top">
            <div class="set-box" v-show="setPanelShow">
              
              <!-- 统计，说明 -->
              <h5 class="set-title">
                <span>
                  点击图标，启用/取消搜索引擎&emsp;&emsp;
                </span>
                <span id="busuanzi_container_site_pv">
                  搜索统计：<span id="busuanzi_value_site_pv"></span> 次
                </span>
              </h5>

              <!-- 浏览器列表 -->
              <div class="browser-list-box clearfix">
                <div v-for="bsItem in browserArr" class="browser-item" @click="switchBrowser($event)">
                  <div class="browser-icon" :style="bsItem.active == true ? bsItem.bgColor : ' '">
                    <i :class="bsItem.icon"></i>
                  </div>
                  <h4 class="browser-name" :class="bsItem.active == true ? 'active' : ' '">{{bsItem.name}}</h4>
                </div>
              </div>

              <!-- 扫一扫 -->
              <div class="erwema-box">
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
              </div>

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
        <div class="result-iframe-content">
          <transition-group name="to-bottom">
            <iframe class="iframe-item" 
            ref="resultPanel"
            v-for="resultItemFrame in browserArr" 
            v-if="resultItemFrame.icon != 'icon-add' && resultItemFrame.active == true" 
            v-show="resultItemFrame.current == true"
            :key="resultItemFrame.icon"
            :style="resultItemFrame.name == '必应' ? 'margin-top: -35px' : ''"
            :searchUrl = "resultItemFrame.browserUrl"
            :current = "resultItemFrame.current"
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
      <div class="hd-broser-list" v-show="searchResultShow">
        <transition-group name="to-bottom">
          <div class="item" 
          :key="bsItem.name"
          :class="bsItem.use == true ? 'active hover-bg' : ''"
          v-for="bsItem in browserArr" 
          v-if="bsItem.icon != 'icon-add' && bsItem.active == true"
          @click="changeBrowser($event)">
            <i :class="bsItem.icon"></i>
            {{bsItem.name}}
          </div>
        </transition-group>
      </div>
    </transition>


    <!-- 背景 -->
    <div class="sm-bg" v-show="!searchResultShow">
      <video :class="bgBlur == true && searchResultShow == false ? 'blur' : ''" v-if="bgObj.type == 'video'" class="bg"  muted autoplay loop :src="bgObj.src"></video>
      <img :class="bgBlur == true && searchResultShow == false ? 'blur' : ''" v-if="bgObj.type == 'image'" class="bg" :src="bgObj.src"/>
    </div>

    <!-- 弹窗 -->
    <AlertBox :alertObj="alertObj"></AlertBox>
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
        // type: 'video',
        // src: 'http://hackbinimg.luokangyuan.com/banner/winter1.mp4',
        type: 'image',
        src: ''
        // src: 'http://hackbinimg.luokangyuan.com/searchmore/bg1.jpg'
      },
      // 控制设置面板显示
      setPanelShow: false,
      // 搜索结果展示
      searchResultShow: false,
      // 输入框下拉关键词
      searchTipShow: false,
      searchTipArr: [],
      // 背景模糊
      bgBlur: false,
      // 搜索引擎列表
      browserArr: [
        {
          name: "百度",
          browserUrl: 'https://www.baidu.com/s?wd=',
          icon: "icon-baidu",
          bgColor: "background: #3385ff",
          use: true, // 是否首选项
          active: true, // 是否激活可用状态
          current: true, // 是否为当前选中显示搜索结果页面
        },
        {
          name: "谷歌",
          browserUrl: 'https://google.suanfazu.com/search/?q=',
          icon: "icon-google",
          bgColor: "background: rgb(251, 188, 5)",
          use: false,
          active: true,
          current: false,
        },
        {
          name: "360",
          browserUrl: 'https://www.so.com/s?q=',
          icon: "icon-360",
          bgColor: "background: #1bc550",
          use: false,
          active: true,
          current: false,
        },
        {
          name: "搜狗",
          browserUrl: 'https://www.sogou.com/web?query=',
          icon: "icon-sougou",
          bgColor: "background: #f84a19",
          use: false,
          active: true,
          current: false,
        },
        {
          name: "必应",
          browserUrl: 'https://cn.bing.com/search?q=',
          icon: "icon-bing",
          bgColor: "background: #007daa",
          use: false,
          active: true,
          current: false,
        },
        {
          name: "手动添加",
          browserUrl: '',
          icon: "icon-add",
          bgColor: "background: red",
          use: false,
          active: true,
          current: false,
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
    // 鼠标覆盖设置图标
    showSetPanel () {
      this.setPanelShow = true;
    },
    hideSetPanel () {
      this.setPanelShow = false;
    },

    // 搜索事件
    searchNow () {
      var searchTxt = this.searchTxt;
      if (searchTxt != ''){
        this.searchResultShow = true;
        this.$refs.onSearch.classList.add("on-search");
        var resultIframeObjArr = this.$refs.resultPanel;
        for (var i=0; i<resultIframeObjArr.length; i++) {
          var thisIfCurrent = resultIframeObjArr[i].getAttribute('current');
          if(thisIfCurrent == 'true') {
            // console.log(resultIframeObjArr[i])
            resultIframeObjArr[i].setAttribute('src', resultIframeObjArr[i].getAttribute("searchUrl") + searchTxt);
          }
        }
      } else {
        this.alertObj.show = true;
        this.alertObj.text = "请输入搜索内容";
        this.alertObj.textEn = "no tm empty, ok?";
      }
    },

    // 输入框enter
    inputEnter () {
      this.searchTipShow = false;
      this.searchNow();
      
    },

    // 聚焦输入框
    inputFocus () {
      this.bgBlur = true;
    },
 
    // 输入框失去焦点
    inputLeave () {
      this.searchTipShow = false;
      this.bgBlur = false;
    },

    // 切换搜索引擎
    changeBrowser (e) {
      // 切换按钮激活状态
      var thisNode = e.currentTarget;
      var p = thisNode.parentNode.children;
      var thisBrowserName = thisNode.innerText.trim();

      // 添加激活
      thisNode.classList.add("active");
      thisNode.classList.add("hover-bg");
      // 取消其他激活
      for ( var i =0,pl= p.length;i<pl;i++) {
        if (p[i] !== thisNode) {
          p[i].classList.remove("active");
          p[i].classList.remove("hover-bg");
        };
      }
    
      // 切换显示iframe
      var thisTxt = thisNode.innerText.trim();
      // 剔除添加按钮
      for ( var j = 0; j < this.browserArr.length - 1; j++) {
        if(this.browserArr[j].name == thisTxt) {
          this.browserArr[j].current = true;
        } else {
          this.browserArr[j].current = false;
        }
      }
    },

    // 开启、关闭搜索引擎
    switchBrowser (e) {
        var thisTxt = e.currentTarget.innerText.trim();
        // 剔除添加按钮
        for ( var k = 0; k < this.browserArr.length - 1; k++) {
          if(this.browserArr[k].name == thisTxt) {
            this.browserArr[k].active = !this.browserArr[k].active;
          }
        }
    },

    // 百度提示
    getBaidu () {
      this.$http.jsonp('https://sp0.baidu.com/5a1Fazu8AA54nxGko9WTAnF6hhy/su',
      {
        params:{wd:this.searchTxt},
        jsonp:'cb'
      })
      .then(function(res){
        if(res.data.s.length > 0) {
          this.searchTipArr = res.data.s;
          this.searchTipShow = true;
        } else {
          this.searchTipShow = false;
        }
      },function(res){
          console.log(res);
      });
    },

    // 选中百度提示
    selectTip (e) {
      this.searchTxt = e.currentTarget.innerText.trim();
      this.searchNow();
      this.searchTipShow = false;
    },


    // 键盘上
    tipUp () {
      console.log('上')
    },

    //键盘下
    tipDown () {
      console.log('下');
      
    },

  },
  // 判断移动端
  beforeCreate () {
    if ((navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))) {
      alert("不想写移动端，百度不好用吗？");
      window.location.href="https://www.baidu.com";
    }

    // 必应壁纸
    this.$http.jsonp('http://bing.ioliu.cn/v1',
      {
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
.hover-bg{
  transition: @animateTime;
  &:hover{
    background: #cc1212 !important;
  }
}


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
        .searchmore-input-text{
          vertical-align: middle;
          border: none;
          width: 520px;
          height: 40px;
          border: 1px solid #cfcfcf;
          border-right:none;
          background: rgba(255, 255, 255, 0.8);
          line-height: 40px;
          padding: 0 40px 0 10px;
          transition:@animateTime;
          font-size: 16px;
          float: left;
          outline: none;
          &:focus{
            // box-shadow: 0px 0px 20px #fdeaea inset;
            border-color: rgb(247, 74, 74);
          }
        }
        .searchmore-submit{
          display: block;
          width: 120px;
          height: 40;
          line-height: 40px;
          text-align: center;
          color: #fff;
          white-space: nowrap;
          overflow: hidden;
          background: @main-color;
          user-select: none;
          float: left;
          cursor: pointer;
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
          background:rgba(255,255, 255, 0.8);
          box-shadow: @box-shadow;
          padding: 20px 20px 2px;
          .browser-list-box{
            display:flex;
            flex-wrap: nowrap;
            margin-top:5px;
            overflow-x: auto;
            .browser-item{
              display:flex;
              flex-direction: column;
              flex-shrink: 0;
              align-items:center;
              justify-content:center;
              width: 100px;
              height: 100px;
              // border:1px solid #000;
              cursor: pointer;
              float: left;
              .browser-icon{
                display:flex;
                justify-content:center;
                align-items:center;
                border-radius: 50%;
                color: #fff;
                width: 50px;
                height: 50px;
                background: #b1b1b1;
                font-size: 30px;
                transition:@animateTime;
              }
              .browser-name{
                margin-top:10px;
                color: #b1b1b1;
                &.active{
                  color: #4e4e4e;
                }
              }
              &:hover{
                .browser-icon{
                  transform: translateY(-3px);
                }
              }
            }
          }
          .erwema-box{
            margin-top:10px;
            border-top: 1px solid #afafaf;
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
          }
          .erweima-title{
            text-align: center;
            color: @main-txt-color;
            font-size: 14px;
          }
        }
      }
      .search-tip{
        position: absolute;
        width: 520px;
        margin-top: 40px;
        background: rgba(255, 255, 255, 0.8);
        box-shadow: @box-shadow;
        li{
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
            span{
              transform: translateX(5px);
            }
            background-color: #ffffff;
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
  .hd-broser-list{
    position: fixed;
    z-index: 60;
    height: 50px;
    display: flex;
    align-items: center;
    left: 780px;
    .item{
      float: left;
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
    background: #f1f1f1;
    .bg{
      transition: @animateTime;
      &.blur{
        filter: blur(2px) brightness(80%);
      }
      width: 102%;
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
}
</style>
