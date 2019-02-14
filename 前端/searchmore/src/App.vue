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
          v-if="indexParent == 0"
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
              @click="searchNow(index)"
            >搜索{{searchObj.name}}</div>
            <!-- 其他搜索类型 -->
            <!-- <div class="more-search-opts" @mouseover="moreSearchShow = true" @mouseleave="moreSearchShow = false">
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
            </div> -->
          </label>

          <!-- 三根聚焦时的线 -->
          <!-- <div class="border-line line-top" :class="inputFocusBool == true ? 'focus' : ''"></div>
          <div class="border-line line-bottom" :class="inputFocusBool == true ? 'focus' : ''"></div> -->
          <div class="line-left" :class="inputFocusBool == true ? 'focus' : ''"></div>

          <!-- 背景图片模糊效果 -->
          <div v-show="!searchResultShow" class="input-blurbg" :style="{backgroundImage: 'url(' +bgObj.src+ ')'}"></div>
        </div>
        <!-- 其他搜索类型 -->
        <div class="more-search-opts" @mouseover="moreSearchShow = true" @mouseleave="moreSearchShow = false">
          <div class="more-trigger hover-bg mid-center">
            <i class="icon-xiala"></i>
          </div>
          <transition name="to-top">
            <div class="more-search-list" v-show="moreSearchShow">
              <ul>
                <li  class="ms-item hover-bg mid-center"
                v-for="(searchObj, index) in browserArr"
                v-if="searchObj.active"
                @click="searchNow(index)">{{searchObj.name}}</li>
              </ul>
            </div>
          </transition>
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
            <!-- 背景图片模糊效果 -->
            <!-- <div v-show="!searchResultShow" class="bg" :style="{backgroundImage: 'url(' +bgObj.src+ ')'}"></div> -->
          </div>
        </transition>

        <!-- 设置页面 -->
        <div class="set-panel" 
          @mouseover="setPanelShow = true" 
          @mouseleave="setPanelShow = false"
        >

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
              <draggable 
                v-model="browserArr"
                @end=""
                :options="{
                  group:'inputEearchEngine',
                  animation: 500,
                  handle: '.touch'
                }"
              >
                <div class="browser-list-box clearfix" 
                  v-for="(bsItemObj, indexParent) in browserArr">
                  <div class="touch icon-tuodong"></div>
                  <MySwitch 
                  :switchChecked.sync="bsItemObj.active" 
                  :switchName="bsItemObj.name"
                  :switchId="indexParent"
                  :changeData="bsItemObj"
                  @switchFun = "selectobj"
                  ></MySwitch>
                  <div class="obj-name">
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
              </draggable>
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
            <!-- <iframe class="iframe-item" 
            v-for="resultItemFrame in resultObj.data"
            v-show="resultItemFrame.current == true && resultItemFrame.active == true"
            :key="resultItemFrame.icon"
            :style="resultItemFrame.resultIframeStyle"
            :src="resultItemFrame.resultSrc"
            frameborder="0"></iframe> -->
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
          :class="commonDisplayStyle"
          v-for="(bmItem, indexParent) in bookMark.data" 
          v-if="bmItem.name == '常用'"
          v-show="commonBookMarkShow"
        >
          <draggable 
            v-model="bmItem.data" 
            :options="{
              group:'item',
              animation: 500,
            }"
          >
            <BookMark 
              v-for="(item, indexChild) in bmItem.data" 
              :bookMarkData="item"
              :indexParent="indexParent"
              :indexChild="indexChild"
              :key="indexChild" 
              :bgObj="bgObj"
              @bookMarkCallBack="bookMarkCallBackFun"
            ></BookMark>
          </draggable>
          <!-- <BookMark :bookMarkData="bookMarkAddBtn" :bgObj="bgObj"></BookMark> -->
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
        <!-- <transition-group name="to-bottom" tag="div"> -->
          <div class="item"
          v-for="(bsItem, indexChild) in bsItemArr.data" 
          :key="bsItem.name"
          :class="bsItem.current == true ? 'active hover-bg' : ''"
          v-if="bsItem.icon != 'icon-add' && bsItem.active == true"
          @click="changeBrowser(parentObjName, indexChild)">
            <!-- <i :class="bsItem.icon"></i> -->
            {{bsItem.name}}
          </div>
        <!-- </transition-group> -->
      </div>
    </transition>


    <!-- 背景 -->
    <div class="sm-bg" v-show="!searchResultShow">
      <video :class="{focus:bgBlur}" v-if="bgObj.type == 'video'" class="bg"  muted autoplay loop :src="bgObj.src"></video>
      <img :class="{focus:bgBlur}" v-if="bgObj.type == 'image'" class="bg" :src="bgObj.src"/>
    </div>

    <!-- 弹窗 -->
    <AlertBox :alertObj="alertObj"></AlertBox>

    <!-- 侧边书签 -->
    <transition name="opacityShow">
      <div class="side-book-mark as-opacity" 
        v-if="bookMark.sideDisplay != 2"
        v-show="!searchResultShow"
      >
        <div class="bm-content mid-center">
          <div class="triggrt" :class="{active: subBookMarkShow}">
            <i class="icon-you"></i>
          </div>
          <!-- 大类列表 -->
          <div class="bm-list-box clearfix"
            @mouseover="subBookMarkShow = true" 
            @mouseleave="subBookMarkShow = false"
          >
            <ul class="bm-list"
            >
              <draggable 
                v-model="bookMark.data"
                @end=""
                :options="{
                  group:'leftBookMark',
                  animation: 500,
                  handle: '.touch'
                }"
              >
                <li  
                  draggable="true" 
                  class="bm-item" 
                  v-for="(bmItem, index) in bookMark.data" 
                  @mouseover="hoverBookMark(index, $event)"
                >
                  <div class="touch icon-tuodong"></div>
                  <i :class="bmItem.icon"></i>
                  <span class="menu-name">{{bmItem.name}}</span>
                </li>
              </draggable>
            </ul>
          </div>

          <!-- 子类弹出框 -->
          <!-- v-show="subBookMarkShow" -->
          <transition name="to-left">
            <div class="sub-list-content" 
              v-show="subBookMarkShow"
              @mouseover="subBookMarkShow = true" 
              @mouseleave="subBookMarkShow = false"
              
            >
              <div class="sub-list-box" ref="subListBox" :style="subBookMarkPosition" :class="{whitebg: searchResultShow}">

                <div class="content-box" v-for="(bmItem, indexParent) in bookMark.data" v-show="bmItem.show">
                  <div class="sub-box-title">
                    {{bmItem.name}}
                  </div>
                    <div class="submenu-list-box" key="indexParent" v-show="bmItem.show">
                      <draggable 
                        v-model="bmItem.data" 
                        :options="{
                          group:'item',
                          animation: 500,
                        }"
                      >
                          <BookMark :bookMarkData="item" :key="index"  v-for="(item, index) in bmItem.data"></BookMark>
                      </draggable>
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

    <!-- 个人   -->
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
          <!-- 头像 -->
          <div class="uhead">
            <!-- <div class="head min-ccenter" style="backgroundImage:url(http://hackbinimg.luokangyuan.com/20181026091093/wei-xin-tu-pian-_20181026095000.jpg)"> -->
            <div class="head min-ccenter">
              <!-- <img class="head-img" :src="bgObj.src" alt="" srcset=""> -->
              <img class="head-img" src="http://hackbinimg.luokangyuan.com/20181026091093/wei-xin-tu-pian-_20181026095000.jpg" alt="" srcset="">
            </div>
            <div class="umsg">
              <h3>杉木搜索 · 更多结果</h3>
            </div>
          </div>
          <!-- 模糊背景 -->
          <div class="head-bg mid-center">
            <!-- <div class="bg" :style="{backgroundImage:'url(' +bgObj.src+ ')'}"></div> -->
            <div class="bg" style="backgroundImage:url(http://hackbinimg.luokangyuan.com/20181026091093/wei-xin-tu-pian-_20181026095000.jpg)"></div>
          </div>
          <!-- 设置列表 -->
          <div class="u-settings">
            <ul>
              <li>
                <span>
                  <i class="icon-bookmark"></i>
                  <span class="name">常用收藏</span>
                </span>
                <MySwitchThree :activeIndex="bookMark.commonDisplay" @selectSwitchThree="commonBookMarkChange">
                  <span slot="val-one" title="展示首页收藏">显示</span>
                  <span slot="val-two" title="首页收藏设为透明，鼠标覆盖显示">透明</span>
                  <span slot="val-three" title="彻底关闭首页收藏">关闭</span>
                </MySwitchThree>
              </li>
              <li title="是否显示侧边收藏夹入口">
                <span>
                  <i class="icon-bookmarks"></i>
                  <span class="name">侧边收藏</span>
                </span>
                <MySwitchThree :activeIndex="bookMark.sideDisplay" @selectSwitchThree="sideBookMarkChange">
                  <span slot="val-one">侧边</span>
                  <span slot="val-two">图标</span>
                  <span slot="val-three">关闭</span>
                </MySwitchThree>
              </li>
              <li class="cuosor_p hover-eff" title="所有的设置项"
                @click="changeSuperSetting"
              >
                <span>
                  <i class="icon-cogs"></i>
                  <span class="name">超级设置</span>
                  <span class="des">所有可配置项</span>
                </span>
              </li>
              <li class="cuosor_p hover-eff" title="详细使用帮助">
                <span>
                  <i class="icon-deng"></i>
                  <span class="name">使用帮助</span>
                </span>
              </li>
            </ul>
          </div>
          <!-- 按钮 -->
          <div class="send-data mid-center">
            <!-- <myButton :btnType="'green'" @myBtnFun="sendData">备份数据</myButton>
            <myButton :btnType="'blue'" @myBtnFun="recData">恢复数据</myButton> -->
            <myButton :btnType="'red'" @myBtnFun="login">登 录</myButton>
          </div>
          <!-- 退出登录 -->
          <div class="u-logout" @click="logOut">
            <i class="icon-switch"></i>
            <span>登出</span>
          </div>
          <!-- 登录提示 -->
          <!-- <div class="login-tip">

          </div> -->
        </div>
      </transition>
    </div>

    <!-- 超级设置页面 -->
    <transition name="to-top">
      <div class="setting-all" v-show="superSettingShow">
        <div class="set-panel mid-center">
          <div class="set-box">
            <!-- 头部 -->
            <div class="title mid-center">
              <div class="setting-title">
                <div class="item mid-center" 
                  v-for="(item, index) in superSettingMenu"
                  :class="{active:item.active}"
                  @click="changeSupperMenu(index)"
                >
                  {{item.name}}
                </div>
              </div>
              <div class="close mid-center"
                @click="changeSuperSetting"
              >
                <i class="icon-cha"></i>
              </div>
            </div>
            <!-- 中间 -->
            <div class="content">
              <!-- 书签 -->
              <transition name="to-left">
                <div class="setting-page superset-book-mark" v-show="superSettingMenu[0].active">
                  <draggable 
                    v-model="bookMark.data" 
                    @start="drag=true" 
                    @end="bookMarkTypeDragEnd"
                    :options="{
                      group:'bookmarkType',
                      animation: 500,
                      handle: '.title',
                      scroll: true,
                      chosenClass: 'choosed'
                    }"
                  >
                    <div class="setting-row-item" v-for="(bmItem, indexParent) in bookMark.data">
                      <!-- 分类标题 -->
                      <div class="title mid-center" title="鼠标按住进行拖动">
                        <i class="icon-tuodong"></i>
                        <div class="name">{{bmItem.name}}</div>
                      </div>
                      <!-- 操作分类 -->
                      <div class="mid-center ignore-drag edit-type" title="编辑本分类名称/图标">
                        <div class="type-edit-item mid-center add" @click="addType(indexParent)">
                          <i class="icon-add mid-center"></i>
                        </div>
                        <div class="type-edit-item mid-center edit" title="修改本分类" @click="editType(indexParent)">
                          <i class="icon-yumaobi mid-center"></i>
                        </div>
                        <div class="type-edit-item mid-center delete" title="删除本分类" @click="delType(indexParent)">
                          <i class="icon-shanchu mid-center"></i>
                        </div>
                      </div>

                      <!-- 子类 -->
                      <div class="item-box">
                        <draggable 
                          v-model="bmItem.data"
                          @end=""
                          :options="{
                            group:'bookmarkItem',
                            animation: 500,
                            filter: '.ignore-drag'
                          }"
                        >
                          <!-- 循环 -->
                          <div class="bm-item" v-for="(item, indexChild) in bmItem.data">
                            <div class="name mid-center">
                              {{item.name}}
                            </div>
                            <!-- 操作 删除，修改 -->
                            <div class="ctrl-box">
                              <!-- 编辑 -->
                              <div class="item edit mid-center" @click="editTypeItem(indexParent, indexChild)">
                                <i class="icon-yumaobi"></i>
                              </div>
                              <!-- 删除 -->
                              <div class="item delete mid-center" @click="delTypeItem(indexParent, indexChild)" title="删除" >
                                <i class="icon-shanchu"></i>
                              </div>
                            </div>
                          </div>

                          <!-- 添加 -->
                          <div class="bm-item mid-center ignore-drag add" title="添加本分类内容" @click="addTypeItem(indexParent, indexChild)">
                            <i class="icon-add"></i>
                          </div>
                        </draggable>
                      </div>
                    </div>
                  </draggable>
                </div>
              </transition>
              <!-- 搜索引擎 -->
              <transition name="to-left">
                <div class="setting-page superset-search" v-show="superSettingMenu[1].active">
                  <draggable 
                      v-model="bookMark.data" 
                      @start="drag=true" 
                      @end="bookMarkTypeDragEnd"
                      :options="{
                        group:'engineType',
                        animation: 500,
                        handle: '.title',
                        scroll: true
                      }"
                    >
                    <div class="setting-row-item" v-for="(brItem, indexParent) in browserArr">
                      <!-- 分类标题 -->
                      <div class="title mid-center">
                        <i class="icon-tuodong"></i>
                        <div class="name">{{brItem.name}}</div>
                      </div>
                      <!-- 操作分类 -->
                      <div class="mid-center ignore-drag edit-type" title="编辑本分类名称/图标">
                        <div class="type-edit-item mid-center add" @click="addType(indexParent)">
                          <i class="icon-add mid-center"></i>
                        </div>
                        <div class="type-edit-item mid-center edit" title="修改本分类" @click="editType(indexParent)">
                          <i class="icon-yumaobi mid-center"></i>
                        </div>
                        <div class="type-edit-item mid-center delete" title="删除本分类" @click="delType(indexParent)">
                          <i class="icon-shanchu mid-center"></i>
                        </div>
                      </div>

                      <!-- 子类 -->
                      <div class="item-box">
                        <draggable 
                          v-model="brItem.data"
                          @end=""
                          :options="{
                            group:'bookmarkItem',
                            animation: 500,
                            filter: '.ignore-drag'
                          }"
                        >

                          <!-- 循环 -->
                          <div class="bm-item mid-center" v-for="(item, index) in brItem.data">
                            <div class="name mid-center">
                              {{brItem.name}}
                            </div>
                             <!-- 操作 删除，修改 -->
                            <div class="ctrl-box">
                              <div class="item edit mid-center">
                                <i class="icon-yumaobi"></i>
                              </div>
                              <div class="item delete mid-center">
                                <i class="icon-shanchu"></i>
                              </div>
                            </div>
                          </div>
                          <!-- 添加 -->
                          <div class="bm-item mid-center ignore-drag add" title="添加本分类内容">
                            <i class="icon-add"></i>
                          </div>
                        </draggable>
                      </div>
                    </div>
                  </draggable>
                </div>
              </transition>
              <!-- 操作指南 -->
              <transition name="to-left">
                <div class="setting-page superset-search" v-show="superSettingMenu[2].active">
                  <br><br>
                  <div class="mid-center">
                    <img width="30%" src="../src/assets/images/zhinanzhen.png" alt="" srcset="">
                  </div>
                </div>
              </transition>
            </div>
          <!-- 底部 -->
            <div class="btn-box mid-center">
              <myButton :btnType="'red'" @myBtnFun="sendData">放弃编辑</myButton>
              <!-- <myButton :btnType="'blue'" @myBtnFun="sendData" class="ml20">添加分类</myButton> -->
              <myButton :btnType="'green'" @myBtnFun="sendData" class="ml20">保存设置</myButton>
            </div>
          </div>
          <!-- 背景图片模糊效果 -->
          <div class="bg" :style="{backgroundImage: 'url(' +bgObj.src+ ')'}"></div>
        </div>
      </div>
    </transition>

    <!-- 添加，编辑书签 -->
    <transition name="to-top">
      <div class="bookmark-edit-add" v-if="bookMarksEditAddShow">
        <div class="bm-ea-box">
          <div class="bm-ea-content">
            <!-- 预览 -->
            <div class="preview-box mid-center">
              <BookMark 
                :bookMarkData="bookMark.data[singleBookMarkData.indexParent].data[singleBookMarkData.indexChild]"
                :edit='false'
              ></BookMark>
            </div>

            <div class="bm-ea-set-detail">
              
              <!-- 名称 -->
              <div class="bm-ea-detail-box">
                <span class="name">网站名称</span>
                <input class="input" maxlength="15" type="text" name="" id="" v-model="bookMark.data[singleBookMarkData.indexParent].data[singleBookMarkData.indexChild].name">
              </div>

              <!-- 地址 -->
              <div class="bm-ea-detail-box">
                <span class="name">网站网址</span>
                <input class="input" maxlength="50" type="text" name="" id="" v-model="bookMark.data[singleBookMarkData.indexParent].data[singleBookMarkData.indexChild].url">
              </div>

              <!-- 图标类型设置 -->
              <div class="bm-ea-detail-box">
                <span class="name">图标设置</span>
                <MySwitchThree  :activeIndex="bookMarkType2SwitchThreeType" class="ml8" @selectSwitchThree="selectIconType">
                  <span slot="val-one">图片</span>
                  <span slot="val-two">文字</span>
                  <span slot="val-three">矢量图</span>
                </MySwitchThree>
              </div>
              <!-- 颜色 -->
              <div class="bm-ea-detail-box">
                <span class="name">背景颜色</span>
                <div class="input">
                  <div class="color-item mid-center" 
                    v-for="(item, index) in colorArr"
                    :class="{active: index == bookMarkBgActiveIndex}"
                    :style="{backgroundColor:item}"
                    @click="chooseBookMarkBgColor(index)"
                  ></div>
                </div>
              </div>
              <!-- 文字大小 -->
              <transition name="to-left">
                <div class="bm-ea-detail-box"
                v-if="bookMark.data[singleBookMarkData.indexParent].data[singleBookMarkData.indexChild].iconType != 'image'">
                  <span class="name">文字大小</span>
                  <vue-slider 
                    ref="slider" 
                    v-model="vueSliderValue"
                    v-bind="vueSliderOption"
                    @drag="vueSliderDrag"
                  ></vue-slider>
                </div>
              </transition>

              <!-- 图标类型 -->
              <transition name="to-left">
                <div class="icontype-box mid-center" 
                  v-if="bookMark.data[singleBookMarkData.indexParent].data[singleBookMarkData.indexChild].iconType == 'image'">
                  <!-- 图片 -->
                  <div class="image-box mid-center">
                    <img 
                      :src="bookMark.data[singleBookMarkData.indexParent].data[singleBookMarkData.indexChild].icon" 
                      alt="网站图标">
                    <!-- <img 
                      v-else 
                      src="http://hackbinimg.luokangyuan.com/20180823130898/logoblue.png" 
                      alt="网站图标"> -->
                    <div class="input-img icon-yumaobi mid-center" @click="imgCropperShow = true"></div>
                  </div>
                </div>
              </transition>

              <!-- 文字类型 -->
              <transition name="to-left">
                <div class="txt-type-box" v-if="bookMark.data[singleBookMarkData.indexParent].data[singleBookMarkData.indexChild].iconType == 'text'">
                  <!-- 文字 -->
                  <div class="bm-ea-detail-box">
                    <span class="name">图标文字</span>
                    <input class="input" maxlength="15" type="text" v-model="bookMark.data[singleBookMarkData.indexParent].data[singleBookMarkData.indexChild].icon">
                  </div>
                </div>
              </transition>

              <!-- 矢量图类型 -->
              <transition name="to-left">
                <div class="txt-type-box" v-if="bookMark.data[singleBookMarkData.indexParent].data[singleBookMarkData.indexChild].iconType == 'svg'">
                  <!-- 图标 -->
                  <div class="bm-ea-detail-box">
                    <span class="name">图标选择</span>
                    <input class="input" readonly maxlength="15" type="text" v-model="bookMark.data[singleBookMarkData.indexParent].data[singleBookMarkData.indexChild].icon">
                  </div>
                </div>
              </transition>
            </div>

            <!-- 底部操作按钮 -->
            <div class="bottom-ctrl mid-center">
              <myButton :btnType="'red'" @myBtnFun="bookMarksEditAddShow = false">放弃编辑</myButton>
              <myButton :btnType="'green'" @myBtnFun="sendData" class="ml20">保存设置</myButton>
            </div>
          </div>
          <div class="bm-ea-bg" :style="{backgroundImage: 'url(' +bgObj.src+ ')'}"></div>
        </div>
      </div>
    </transition>

    <!-- 图片裁剪 -->
    <!-- :img="bookMark.data[0].data[1].icon" -->
    <imgCropper
      :imgCropperShow="imgCropperShow"
      :img="bookMark.data[singleBookMarkData.indexParent].data[singleBookMarkData.indexChild].icon"
      @cropperCallBack = "imgCropperCallBack"
    ></imgCropper>
  </div>
</template>

<script>
import draggable        from 'vuedraggable'
import AlertBox         from '../src/components/alert.vue'
import BookMark         from '../src/components/bookMark.vue'
import MySwitch         from '../src/components/switch.vue'
import myButton         from '../src/components/myButton.vue'
import MySwitchThree    from '../src/components/switchThree.vue'
import imgCropper       from '../src/components/imgCropper' 
import vueSlider from 'vue-slider-component'

export default {
  name: 'App',
  components: {
    AlertBox,       // 弹窗
    BookMark,       // 书签小组件
    MySwitch,       // 双相开关
    myButton,       // 圆角按钮
    MySwitchThree,  // 三相开关
    draggable,      // 拖动插件
    imgCropper,     // 图片裁剪
    vueSlider,      // 滑动组件
  },
  data () {
    return {
      // 背景
      bgObj: {
        type: 'image',
        src: ''
        // src: 'http://hackbinimg.luokangyuan.com/searchmore/bg1.jpg'
      },

      // 编辑添加书签 样式 临时变量
      itemStyleObjTmp: {"fontSize":"24px","background":"#000"},

      // 滑动组件的默认值
      vueSliderValue: 24,
      // 滑动组件初始化值
      vueSliderOption: {
        width: 212,
        height: 5,
        dotSize: 20,
        interval: 2,
        tooltipDir: 'right',
        min: 12,
        max: 40,
      },
      // 变价添加书签，背景颜色选中index
      bookMarkBgActiveIndex: -1,
      // 输入框聚焦
      inputFocusBool: false,
      
      // 输入框设置显示
      setPanelShow: false,

      // 超级设置显示
      superSettingShow: false,
      // 超级设置菜单内容
      superSettingMenu :[
        {
          name: "书签设置",
          active: true,
        },
        {
          name: "搜索设置",
          active: false,
        },
        {
          name: "操作指南",
          active: false,
        },
      ],

      // 搜索结果展示
      searchResultShow: false,

      // 单个书签 添加编辑显示控制
      bookMarksEditAddShow: false,
      // 单个书签显示时 传入的数据
      singleBookMarkData: {
        indexParent: 0,
        indexChild: 0,
      },

      // 图片裁剪显示
      imgCropperShow: false,

      // 书签子类展示
      subBookMarkShow: false,

      //子书签Y位置
      subBookMarkPosition: 'top:0px',

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

      // 上传图片控件显示
      imgUploadShow: false,
      
      // 颜色
      colorArr: [
        '#2195f2',
        '#03a8f3',
        '#00bbd3',
        '#009587',
        '#4bae4f',
        '#8ac249',
        '#ccdb39',
        '#fec007',
        '#fe9700',
        '#fe5622',
        '#f34236',
        '#9b27af',
        '#663ab6',
        '#fff',
        'rgba(0, 0, 0, 0.3)',
      ],

      // 搜索引擎列表
      browserArr: [
        {
          name:'网页', // 两个字
          active: true, // 是否可用
          current: true, // 是否当前搜索的项，不用传递到后台
          data:[
            {
              name: "百度",
              url: 'https://www.baidu.com/s?wd=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-baidu",
              iconType: 'svg',
              active: true, // 是否激活可用状态
              current: true, // 是否为当前选中显示搜索结果页面
              iconColor: "#3385ff", // 图标颜色
              resultIframeStyle: { //搜索结果的单独样式
              },
            },
            {
              name: "Yandex",
              url: 'https://yandex.com/search/?text=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-google",
              iconType: 'svg',
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
              url: 'https://www.so.com/s?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-360",
              iconType: 'svg',
              active: true,
              current: false,
              iconColor: "#1bc550",
              resultIframeStyle: {},
            },
            {
              name: "搜狗",
              url: 'https://www.sogou.com/web?query=',
              resultSrc:'',
              icon: "icon-sougou",
              iconType: 'svg',
              bgColor: "background: #f84a19",
              active: true,
              current: false,
              iconColor: "#f84a19",
              resultIframeStyle: {},
            },
            {
              name: "必应",
              url: 'https://cn.bing.com/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              iconType: 'svg',
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
          data:[
            {
              name: "百度1",
              url: 'https://image.baidu.com/search/index?tn=baiduimage&word=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-baidu",
              iconType: 'svg',
              active: true, // 是否激活可用状态
              current: true, // 是否为当前选中显示搜索结果页面
              iconColor: "#3385ff", // 图标颜色
              resultIframeStyle: { //搜索结果的单独样式
              },
            },
            {
              name: "谷歌1",
              url: 'https://google.suanfazu.com/search/?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-google",
              iconType: 'svg',
              active: true,
              current: false,
              iconColor: "rgb(251, 188, 5)",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-53px',
                height:'calc(100% + 53px)'
              },
            },
            {
              name: "3601",
              url: 'http://image.so.com/i?q=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-360",
              iconType: 'svg',
              active: true,
              current: false,
              iconColor: "#1bc550",
              resultIframeStyle: {},
            },
            {
              name: "搜狗1",
              url: 'https://pic.sogou.com/pics?query=',
              resultSrc:'',
              icon: "icon-sougou",
              iconType: 'svg',
              active: true,
              current: false,
              iconColor: "#f84a19",
              resultIframeStyle: {},
            },
            {
              name: "必应1",
              url: 'https://cn.bing.com/images/search?q=',
              resultSrc:'',
              icon: "icon-bing",
              iconType: 'svg',
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
          name:'音乐',
          data:[
            {
              name: "酷狗",
              url: 'https://www.kugou.com/yy/html/search.html#searchType=song&searchKeyWord=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-baidu",
              iconType: 'svg',
              active: true, // 是否激活可用状态
              current: true, // 是否为当前选中显示搜索结果页面
              iconColor: "#3385ff", // 图标颜色
              resultIframeStyle: { //搜索结果的单独样式
              },
            },
            {
              name: "网易云音乐",
              url: 'https://music.163.com/#/search/m/?s=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-google",
              iconType: 'svg',
              active: true,
              current: false,
              iconColor: "rgb(251, 188, 5)",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-53px',
                height:'calc(100% + 53px)'
              },
            },
            {
              name: "QQ音乐",
              url: 'https://y.qq.com/portal/search.html#page=1&searchid=1&remoteplace=txt.yqq.top&t=song&w=',
              resultSrc:'',// 搜索结果的链接
              icon: "icon-360",
              iconType: 'svg',
              active: true,
              current: false,
              iconColor: "#1bc550",
              resultIframeStyle: {},
            },
            {
              name: "千千音乐",
              url: 'http://music.taihe.com/search?key=',
              resultSrc:'',
              icon: "icon-sougou",
              iconType: 'svg',
              active: true,
              current: false,
              iconColor: "#f84a19",
              resultIframeStyle: {},
            },
            {
              name: "虾米音乐",
              url: 'https://www.xiami.com/search?key=',
              resultSrc:'',
              icon: "icon-bing",
              iconType: 'svg',
              active: true,
              current: false,
              iconColor: "#007daa",
              resultIframeStyle: { //搜索结果的单独样式
                marginTop: '-35px',
                height:'calc(100% + 35px)'
              },
            }
          ]
        }
      ],

      // 书签添加
      bookMarkAddBtn: {
        name: '添加',
        icon: 'icon-add',
        iconType:'svg', // 图标类型
        style:'{"fontSize":"24px","color":"red","background":"#22be51"}',
        url:'javascript:;'
      },
      // 书签
      bookMark: {
        sideDisplay: 0,    // 控制侧边书签显示方式，0：显示入口，1：仅显示图标，2：完全隐藏
        commonDisplay: 0 , // 控制常用书签是否显示，0：正常显示，1：透明，但鼠标覆盖显示，2：完全隐藏
        position: 3,       //侧边书签位置，0,1,2,3 上右下左
        data:[
          {
            name:'热爱学习', // 三个字以内
            icon:'icon-zaixianxuexi',
            order:'1',
            show: true,
            data: [
              {
                name: 'csdn',
                icon: 'csdn',
                iconType:'text', // 图标类型
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: 'vue',
                icon: 'https://cn.vuejs.org/images/logo.png',
                iconType:'image',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: 'React',
                icon: 'React',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
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
                iconType:'text',
                order: 1,
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '生活',
                icon: '生活',
                iconType:'text',
                order: 2,
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '生活',
                icon: '生活',
                iconType:'text',
                order: 3,
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
            ]
          },
          {
            name:'工作',
            icon:'icon-display',
            order:'3',
            show: false,
            data: [
              {
                name: '工作',
                icon: '工作',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '工作',
                icon: '工作',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '工作',
                icon: '工作',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
            ]
          },
          {
            name:'工作4',
            icon:'icon-bug',
            order:'4',
            show: false,
            data: [
              {
                name: '工作',
                icon: '工作',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '工作',
                icon: '工作',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                bgColor:'',
                url:'http://w3more.cn/'
              },
              {
                name: '工作',
                icon: '工作',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
            ]
          },
          {
            name:'工作6',
            icon:'icon-pie-chart',
            order:'6',
            show: false,
            data: [
              {
                name: '工作',
                icon: '工作',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '工作',
                icon: '工作',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '工作',
                icon: '工作',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
            ]
          },
          {
            name:'工作5',
            icon:'icon-calendar',
            order:'5',
            show: false,
            data: [
              {
                name: '工作',
                icon: '工作',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '工作',
                icon: '工作',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '工作',
                icon: '工作',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
            ]
          },
          {
            name:'常用',
            icon:'icon-telegram',
            order:'6',
            show: false,
            data: [
              {
                name: 'vue官网',
                icon: 'http://hackbinimg.luokangyuan.com/20181026091093/wei-xin-tu-pian-_20181026095000.jpg',
                iconType:'image',
                style:'{"fontSize":"24px","color":"red"}',
                url:'https://cn.vuejs.org/'
              },
              {
                name: 'github',
                icon: 'Git',
                iconType:'text',
                style:'{"fontSize":"24px","color":"#fff","background":"#de1a1a"}',
                url:'https://github.com/'
              },
              {
                name: '常用',
                icon: '常用',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '常用',
                icon: '常用',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '常用',
                icon: '常用',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '常用',
                icon: '常用',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '常用',
                icon: '常用',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '常用',
                icon: '常用',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '常用',
                icon: '常用',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              },
              {
                name: '常用',
                icon: '常用',
                iconType:'text',
                style:'{"fontSize":"24px","background":"#000"}',
                url:'http://w3more.cn/'
              }
            ]
          },
        ]
      },


      // 搜索输入框值
      searchTxt: '',

      //登录
      login() {
        alert('登录')
      },

      // 备份
      saveData () {
      },

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
    searchNow: function (index) {
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
                currentObj.data[j].resultSrc = currentObj.data[j].url + this.searchTxt;
                // this.$axios.get(currentObj.data[j].resultSrc)
                //   .then(function (response) {
                //   　console.log("成功")
                //     console.log(response)
                //   }).catch(function (error) {
                //   　console.log("失败")
                //   　console.log(error)
                //   });
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
    inputEnter: function (index) {
      this.searchTipObj.show = false;
      this.searchNow(index);
    },

    // 聚焦输入框
    inputFocus: function () {
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
    inputLeave: function () {
      this.searchTipObj.show = false;
      this.bgBlur = false;
      // if(this.searchTipObj.data.length > 0) {
        this.inputFocusBool = false;
      // }
    },

    // 切换搜索引擎
    changeBrowser: function (parentObjName, indexChild) {
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
    addBrowser: function() {
      this.alertObj.show = true;
      this.alertObj.text = "明日开发";
      this.alertObj.textEn = "too young too simple";
    },
    // 开启、关闭搜索引擎
    switchBrowser: function (indexParent, indexChild) {
      this.browserArr[indexParent].data[indexChild].active = !this.browserArr[indexParent].data[indexChild].active;
    },

    // 切换 超级设置 菜单
    changeSupperMenu (index) {
      for(var i=0; i<this.superSettingMenu.length; i++) {
        if(index == i) {
          this.superSettingMenu[i].active = true;
        } else {
          this.superSettingMenu[i].active = false;
        }
      }
    },

    // 百度提示
    getBaidu: function () {
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
    selectTip: function (indexParent, indexChild) {
      this.searchTxt = this.searchTipObj.data[indexChild];
      this.searchNow(indexParent);
      this.searchTipObj.show = false;
      this.searchTipObj.nowIndex = indexChild;
    },

    // 键盘上
    tipUp: function () {
      this.searchTipObj.nowIndex--;
      if(this.searchTipObj.nowIndex <= -1) {
        this.searchTipObj.nowIndex = this.searchTipObj.data.length - 1;
      }
      this.searchTxt = this.searchTipObj.data[this.searchTipObj.nowIndex];
    },

    //键盘下
    tipDown: function () {
      this.searchTipObj.nowIndex++;
      if(this.searchTipObj.nowIndex >= this.searchTipObj.data.length) {
        this.searchTipObj.nowIndex = 0;
      }
      this.searchTxt = this.searchTipObj.data[this.searchTipObj.nowIndex];
    },

    // 输入框 切换首选搜索引擎
    chooseThisEngineAnCurrent: function (indexParent, indexChild) {
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
    selectobj: function (changeVal) {
      changeVal.active = !changeVal.active;
    },

    // 鼠标覆盖便签大类
    hoverBookMark: function (index, e) {
      // 显示子类书签
      for(let i=0; i<this.bookMark.data.length; i++) {
        if(i == index) {
          this.bookMark.data[i].show = true;
        } else {
          this.bookMark.data[i].show = false;
        }
      }
      // 书签高度
      var curPosition = e.currentTarget.getBoundingClientRect();
      setTimeout(() => {
        var subListBoxElPosition = this.$refs.subListBox.getBoundingClientRect();
        // 子书签超出屏幕
        if(subListBoxElPosition.height + curPosition.y > document.body.clientHeight) {
          this.subBookMarkPosition = 'top:' +(curPosition.y - subListBoxElPosition.height + curPosition.height) +'px';
        } 
        // 子书签在屏幕内
        else {
          this.subBookMarkPosition = 'top:' +curPosition.y+ 'px';
        };
      }, 1);
    },

    // 备份数据
    sendData: function () {
      alert('备份数据')
    },

    // 恢复数据
    recData: function () {
      alert('恢复数据')
    },

    // 退出登录
    logOut: function () {
      alert('登出')
    },

    // 切换常用书签
    switchCommonBookMark: function() {
      this.$store.commit('changeBookMarkShow');
    },

    // 切换侧边书签
    switchSideBookMark: function () {
      this.$store.commit('changeSideBookMarkShow');
    },

    // 切换超级设置显示
    changeSuperSetting: function () {
      this.superSettingShow = !this.superSettingShow;
    },


    // ==================点击三相开关==========================
    // 设置图标类型
    selectIconType: function (flag) {
      switch (flag) {
        case 'left':
          this.bookMark.data[this.singleBookMarkData.indexParent].data[this.singleBookMarkData.indexChild].iconType = 'image';
          break;
        case 'center':
          this.bookMark.data[this.singleBookMarkData.indexParent].data[this.singleBookMarkData.indexChild].iconType = 'text';
          break;
        case 'right':
          this.bookMark.data[this.singleBookMarkData.indexParent].data[this.singleBookMarkData.indexChild].iconType = 'svg';
          break;
        default:
          break;
      }
    },
    // 设置常用收藏显示方式
    commonBookMarkChange: function (flag) {
      switch (flag) {
        case 'left':
          this.bookMark.commonDisplay = 0; // 显示
          break;
        case 'center':
          this.bookMark.commonDisplay = 1;  // 透明
          break;
        case 'right':
          this.bookMark.commonDisplay = 2;  // 隐藏
          break;
        default:
          break;
      }
    },
    // 设置侧边收藏显示方式
    sideBookMarkChange: function (flag) {
      switch (flag) {
        case 'left':
          this.bookMark.sideDisplay = 0; // 显示入口
          break;
        case 'center':
          this.bookMark.sideDisplay = 1;  // 透明
          break;
        case 'right':
          this.bookMark.sideDisplay = 2;  // 隐藏
          break;
        default:
          break;
      }
    },

    // 超级设置 拖动书签大类
    bookMarkTypeDragEnd: function() {
      // console.table(this.bookMark.data);
    },

    // 书签组件点击回调
    bookMarkCallBackFun (resObj) {
      switch (resObj.type) {
        // 编辑
        case 'edit':
          this.bookMarksEditAddShow = true;
          this.singleBookMarkData.indexParent = resObj.indexParent;
          this.singleBookMarkData.indexChild = resObj.indexChild;
          this.itemStyleObjTmp = JSON.parse(this.bookMark.data[resObj.indexParent].data[resObj.indexChild].style);
          
          break;
        // 删除
        case 'delete':
          alert('删除');
          break;

        default:
          break;
      }
    },

    // ========== 图片裁剪回调 ============
    imgCropperCallBack: function (opts) {
      var oldBookMarkIcon = this.bookMark.data[this.singleBookMarkData.indexParent].data[this.singleBookMarkData.indexChild].icon;
      // 确认裁剪
      if( opts.active == "cropper") {
        this.bookMark.data[this.singleBookMarkData.indexParent].data[this.singleBookMarkData.indexChild].icon = opts.data;
        this.imgCropperShow = false;
      } else
      // 取消裁剪
      if( opts.active == "cancel" ) {
        this.bookMark.data[this.singleBookMarkData.indexParent].data[this.singleBookMarkData.indexChild].icon = oldBookMarkIcon;
        this.imgCropperShow = false;
      }
    },

    // ==========书签、引擎大类增删改============
    addType: function (index) {
    },
    editType: function (index) {
    },
    delType: function (index) {
    },
    
    // ==========书签、引擎子类增删改============
    addTypeItem: function (indexParent, indexChild) {
    },
    editTypeItem: function (indexParent, indexChild) {
    },
    delTypeItem: function (indexParent, indexChild) {
    },

        
    // 切换书签编辑添加背景颜色
    chooseBookMarkBgColor: function (index) {
      this.bookMarkBgActiveIndex = index;
      this.itemStyleObjTmp.background = this.colorArr[index];
      this.bookMark.data[this.singleBookMarkData.indexParent].data[this.singleBookMarkData.indexChild].style = JSON.stringify(this.itemStyleObjTmp);
    },

    // 拖动滑动条
    vueSliderDrag () {
      this.itemStyleObjTmp.fontSize = this.vueSliderValue + 'px';
      this.bookMark
      .data[this.singleBookMarkData.indexParent]
      .data[this.singleBookMarkData.indexChild].style = JSON.stringify(this.itemStyleObjTmp);
    }
  },
  computed: {
    // 是否展示常用书签
    commonBookMarkShow: function () {
      return !(this.inputFocusBool || this.searchResultShow || this.bookMark.commonDisplay === 2);
    },

    // 常用书签展示方式class
    commonDisplayStyle: function () {
      // 显示
      if (this.bookMark.commonDisplay === 0) {
        return '';
      }
      // 透明
      else if (this.bookMark.commonDisplay === 1) {
        return 'as-opacity';
      } 
      // 关闭
      else {
        return 'hide';
      }
    },

    // 书签类型转化为三相开关
    bookMarkType2SwitchThreeType: function () {
      var itemIconType = this.bookMark.data[this.singleBookMarkData.indexParent].data[this.singleBookMarkData.indexChild].iconType;
      if (itemIconType == 'image') {
        return 0;
      }
      else if (itemIconType == 'text') {
        return 1;
      }
      else if (itemIconType == 'svg') {
        return 2;
      }
    },

  },
  // 判断移动端
  beforeCreate () {
    if ((navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))) {
      alert("检测到你的手机太辣鸡，正在跳转到百度\n请用电脑使用本工具！");
      window.location.href="https://www.baidu.com";
    }

    var wallpapperDay = Math.floor(Math.random() * 100);
    // 必应壁纸 http://bing.ioliu.cn/v1
    this.$http.jsonp('http://bing.ioliu.cn/v1',
      {
        params:{d:wallpapperDay},
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
  mounted: function () {
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
        overflow: hidden;
        // box-shadow: @box-shadow;
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
          z-index: 2;
          vertical-align: middle;
          border: none;
          width: 520px;
          height: 40px;
          // background: transparent;
          background: rgba(255, 255, 255, 0.3);
          box-shadow: 0 0 30px rgba(255, 255, 255, 0.651) inset;
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
        .input-blurbg{
          position: absolute;
          z-index: 1;
          top:0;
          left:0;
          width: 100vw;
          height: 100vh;
          background-attachment: fixed;
          background-repeat: no-repeat;
          background-position: center;
          background-color: rgb(255, 255, 255);
          background-size: 100%;
          transform: translate(-50%, -40vh);
          opacity: .8;
          filter: blur(30px);
        }
        .searchmore-submit{
          position: relative;
          z-index: 2;
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
      .more-search-opts{
        position: fixed;
        z-index: 10;
        left: 520px;
        top:40px;
        color: #fff;
        cursor: pointer;
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
            height: 40px;
            line-height: 40px;
            background: @main-color;
          }
        }
        &:hover{
          .more-trigger{
            background: #cc1212;
            >i{
              transform: translateY(1px) scale(.8);
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
          color: @main-txt-color;
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
          background:rgb(255, 255, 255);
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
        box-shadow: @box-shadow;
        border-bottom: 1px solid @main-color;
        overflow: hidden;
        >ul{
          position: relative;
          z-index: 2;
          background: rgb(255, 255, 255);
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
        .bg{
          position: absolute;
          z-index: 1;
          top:0;
          left:0;
          width: 100vw;
          height: 90vh;
          background-attachment: fixed;
          background-repeat: no-repeat;
          background-position: center;
          background-color: rgb(255, 255, 255);
          background-size: 100%;
          transform: translate(-50%, -40vh);
          filter: blur(30px);
        }
      }
    }
    .comom-book-mark{
      position: fixed;
      left: 50%;
      top: 50vh;
      margin-left: -320px - 4px; // 减去item的margin
      width: 648px + 4px + 13px; // 加上两边margin + 滚动条
      height: 216px;
      overflow-y: auto;
      overflow-x: hidden;
      display: flex;
      flex-wrap: wrap;
      transition: @animateTime;
      &.as-opacity{
        opacity: 0;
        &:hover{
          opacity: 1;
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

  .hd-broser-list{
    position: fixed;
    z-index: 60;
    height: 50px;
    display: flex;
    flex-direction: row;
    align-items: center;
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

  .side-book-mark{
    position:fixed;
    top:0;
    left:0;
    transform: translateX(-99%);
    transform: translateX(0%);
    z-index: 150;
    height: 100%;
    width: 100px;
    // border-right: 1px solid @main-color;
    box-shadow: @box-shadow;
    transition: @animateTime;
    background: rgba(0, 0, 0, 0.3);
    display: flex;
    &:hover{
      transform: translateX(0%);
    }
    .bm-content{
      width:100%;
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
        &.active{
          transform: rotate(180deg);
          margin-left: 50px;
        }
      }
      .bm-list-box{
        position: relative;
        z-index: 120;
        width:100%;
        background: rgba(22, 22, 22, 0.452);
        display: flex;
        align-items: center;
        overflow: hidden;
        .bm-list{
          // height:100%;
          width:100%;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          >div{
            width:100%;
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
              background: rgba(0, 0, 0, 0.7);
              user-select: none;
              .touch,>i{
                height: 50px;
                line-height: 50px;
              }
              .touch{
                margin-right: 2px;
                margin-left:2px;
                color: rgba(209, 209, 209, 0.712);
                &:hover{
                  color: rgb(209, 209, 209);
                  cursor: move;
                }
              }
              .menu-name{
                margin-left: 3px;
                white-space: nowrap;
                font-size: 12px;
              }
              &:first-child{
                border-top: 1px solid rgba(255, 255, 255, 0.1);
              }
              &:hover{
                background: @main-color;
              }
            }
          }
        }
      }
      .sub-list-content{
        position: fixed;
        z-index: 120;
        margin-left: 100px;
        width: 100vw;
        // height:100vh;
        top:0;
        left: 0;
        // background: rgba(0, 0, 0, 0.7);
        // display: flex;
        // align-items: center;
        .sub-list-box{
          position: absolute;
          // min-height:148px;
          max-width: 836px;
          overflow: hidden;
          // padding-left: 20px;
          // box-shadow: 5px 0 50px rgba(0, 0, 0, 0.3);
          // transition: @animateTime;
          &.whitebg{
            background: rgb(255, 255, 255);
          }
          .content-box{
            position: relative;
            width:100%;
            z-index:100;
            display: flex;
            flex-direction: row;
            background:rgba(255, 255, 255, 0.7);
            .sub-box-title{
              display: flex;
              align-items: center;
              justify-content: center;
              width:40px;
              // height: 100%;
              background: @main-color;
              color: #fff;
              font-size: 20px;
              font-weight: bold;
              letter-spacing: 10px;
              writing-mode: vertical-lr;
            }
            .submenu-list-box{
              width:calc(~'100% - 40px');
              // height:100%;
              padding: 20px;
              overflow: auto;
              // height:80vh;
            }
          }
          .bm-bg-box{
            position: absolute;
            top:0;
            left: 20px;
            margin-top: -25%;
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
              background-size: 100%;
              filter: blur(30px);
            }
          }
        }
      }
    }
    &.as-opacity{
      transform: translateX(0%);
      // background: transparent;
      width: 55px;
      border: none;
      box-shadow: 0 0 0 transparent;
      .bm-content{
        .triggrt{
          display: none;
        }
        .bm-list-box{
          background: transparent;
          .bm-list{
            .bm-item{
              background: transparent;
              border: none;
              &:hover{
                i{
                  font-size: 30px;
                  opacity: 1;
                  // color: @main-color;
                }
              }
              i{
                font-size: 30px;
                opacity: .3;
                transition: @animateTime;
                padding-left: 10px;
              }
              .touch{
                display: none;
              }
              .menu-name{
                opacity: 0;
              }
            }
          }
        }
        .sub-list-content{
          margin-left: 55px;
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
      padding: 30px 40px;
      box-shadow: @box-shadow;
      .uhead{
        position: relative;
        z-index: 20;
        width: 100%;
        // height: 185px;
        display: flex;
        flex-direction: column;
        align-items: center;
        .head{
          width:200px;
          height:200px;
          border-radius: 100%;
          overflow: hidden;
          background-size: cover;
          background-position: center;
          background-repeat: no-repeat;
          border: 5px solid rgba(255, 255, 255, 0.4);
          box-shadow: 0 0 30px rgba(0, 0, 0, 0.1);
          transition: @animateTime;
          cursor: pointer;
          // &:hover{
          //   transform: scale(1.5);
          // }
          .head-img{
            width: 100%;
            height: 100%;
          }
        }
        .umsg{
          margin-top: 10px;
          color: #fff;
          text-shadow: 0 0 15px rgba(0, 0, 0, 0.5);
        }
      }
      .head-bg{
        position: absolute;
        z-index: 10;
        top:0;
        left:0;
        width:100%;
        height: 280px;
        overflow: hidden;
        // background: #fff;
        .bg{
          width: 300px;
          height: 280PX;
          transform: scale(1.3);
          background-size: cover;
          background-position: center;
          background-repeat: no-repeat;
          filter: blur(15px) brightness(1.2);
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
            color: @main-txt-color;
            transition: @animateTime;
            &:not(:last-child){
              border-bottom: 1px dotted #dedede;
            }
            &.hover-eff:hover{
              background: linear-gradient(to right,rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.1), rgba(0, 0, 0, 0));
            }
            .name{
              margin-left: 5px;
              font-weight: bold;
            }
            .des{
              margin-left: 3px;
              font-size: 12px;
              color: #8d8d8d;
            }
          }
        }
      }
      .send-data{
        width:100%;
        margin-top: 10px;
        justify-content: space-around;
      }
      .u-logout{
        position:absolute;
        top:10px;
        right: 10px;
        z-index: 10;
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
        text-shadow: 0 0 15px rgba(0, 0, 0, 0.5);
        cursor:pointer;
        transition: @animateTime;
        &:hover{
          color: rgb(255, 255, 255);
        }
      }
      .login-tip{
        position: absolute;
        top:0;
        left:0;
        z-index: 200;
        width:100%;
        height:100%;
        background: radial-gradient(rgba(255, 0, 0, 0.692), rgbargba(255, 0, 0, 0.973));
      }
    }
  }

  .setting-all{
    position:fixed;
    top:0;
    left:0;
    z-index: 200;
    width:100%;
    height: 100%;
    .set-panel{
      position: absolute;
      left:50%;
      margin: 5vh 0 0 -532px;
      width:1064px;
      height:90vh;
      background-color: rgba(255, 255, 255, 0.9);
      box-shadow: @box-shadow;
      overflow: hidden;
      user-select: none;
      .set-box{
        position: absolute;
        z-index: 2;
        top:0;
        left:0;
        width: 100%;
        height:100%;
        background: rgba(255, 255, 255, 0.7);
        color: @main-txt-color;
        display: flex;
        flex-direction: column;
        .title{
          position: relative;
          width:100%;
          height:50px;
          font-size: 18px;
          font-weight: bold;
          background: rgba(255, 255, 255, 0.6);
          flex: 0 0 auto;
          .setting-title{
            height: 100%;
            display: flex;
            .item{
              min-width: 100px;
              height:100%;
              background:rgba(0, 0, 0, 0.1);
              transition: @animateTime;
              &.active{
                background: @main-color;
                box-shadow: 0 0 20px rgba(0, 0, 0, 0.1) inset;
                color: #fff;
                &:hover{
                  background:@main-color-dark;
                }
              }
              &:hover{
                background:rgba(0, 0, 0, 0.2);
              }
              cursor: pointer;
              &:not(:first-child){
                border-left: 1px solid rgba(255, 255, 255, 0.3);
              }
            }
          }
          .close{
            position: absolute;
            right:10px;
            top:10px;
            width: 30px;
            height:30px;
            border-radius: 50%;
            font-size: 12px;
            color: @main-color;
            border: 1px solid @main-color;
            cursor: pointer;
            transition: @animateTime;
            &:hover{
              transform: rotate(180deg);
            }
          }
        }
        .content{
          flex: 1 1 auto;
          overflow-y: auto;
          overflow-x: hidden;
          position: relative;
          .setting-page {
            position:absolute;
            padding: 0 20px;
            transition: @animateTime * 1.5;
            width: 100%;
            .choosed{
              background: rgba(255, 255, 255, .5);
            }
            .setting-row-item{
              display: flex;
              padding: 10px 0;
              margin-top: -1px;
              border-top: 1px dashed rgb(165, 165, 165);
              border-bottom: 1px dashed rgb(165, 165, 165);
              &:last-child{
                border-bottom: none;
              }
              &:first-child{
                border-top: none;
              }
              .title{
                position: relative;
                display: flex;
                height:auto;
                width:34px;
                margin: 4px 0;
                cursor: move;
                flex-direction: column;
                .icon-tuodong{
                  position:absolute;
                  top:-1px;
                  transform: rotate(90deg);
                  font-size: 12px;
                }
                .name{
                  writing-mode:vertical-lr;
                  font-size: 14px;
                }
              }
              .edit-type{
                width: 35px;
                min-width: 35px;
                background:rgba(255, 255, 255, 0.4);
                display: flex;
                margin: 4px 0 4px 1px;
                flex-direction: column;
                .type-edit-item{
                  font-size: 12px;
                  flex-grow: 1;
                  max-height: 35px;
                  width:100%;
                  cursor: pointer;
                  transition: @animateTime;
                  &:hover{
                    background: rgba(0, 0, 0, 0.2);
                    color:#fff;
                  }
                  &.delete{
                    color: @main-color;
                  }
                  &.edit{
                    color: @main-blue;
                  }
                  &.add{
                    color: @main-green;
                  }
                }
              }
              .item-box>div{
                display: flex;
                flex-wrap: wrap;
                height: 100%;
                .bm-item{
                  position: relative;
                  min-width: 70px;
                  height: 70px;
                  padding: 0 10px;
                  background: rgba(255, 255, 255, 0.4);
                  // margin-left: 8px;
                  margin: 4px 0 4px 8px;
                  cursor: pointer;
                  transition: @animateTime;
                  font-weight: bold;
                  overflow: hidden;
                  .name{
                    position:absolute;
                    top:0;
                    left:0;
                    width:100%;
                    height:100%;
                    transition: @animateTime;
                  }
                  .ctrl-box{
                    position: absolute;
                    bottom:0;
                    left:0;
                    width:100%;
                    height:30px;
                    background: rgba(0, 0, 0, 0.2);
                    display: flex;
                    transition: @animateTime;
                    transform: translateY(100%);
                    .item{
                      flex: 1 1 auto;
                      transition: @animateTime;
                      font-size: 14px;
                      &.delete{
                        color: @main-color;
                        border-left: 1px solid rgba(255, 255, 255, 0.2);
                      }
                      &.edit{
                        color: @main-blue;
                      }
                      &:hover{
                        background: rgba(0, 0, 0, 0.05);
                      }
                    }
                  }
                  &:hover{
                    // background: rgba(0, 0, 0, 0.05);
                    .name{
                      transform: translateY(-15px);
                    }
                    .ctrl-box{
                      transform: translateY(0%);
                    }
                  }
                  
                  &.delete{
                    color: @main-color;
                  }
                  &.edit{
                    color: @main-blue;
                  }
                  &.add{
                    color: @main-green;
                  }
                }
              }
            }
          }
        }
        .btn-box{
          width:100%;
          height: 60px;
          flex: 0 0 auto;
          background: rgba(255, 255, 255, 0.6);
        }
      }
      .bg{
        position: absolute;
        z-index: 1;
        top:0;
        left:0;
        margin-left: -50%;
        margin-top: -50%;
        width: 200%;
        height: 200%;
        background-attachment: fixed;
        background-repeat: no-repeat;
        background-position: center;
        background-color: rgb(255, 255, 255);
        background-size: 100%;
        filter: blur(30px);
      }
    }
  }

  .bookmark-edit-add{
    position: fixed;
    top:0;
    left:0;
    z-index: 1000;
    width:100%;
    height:100%;
    .bm-ea-box{
      position: absolute;
      left:50%;
      margin-left: -200px;
      top:50%;
      margin-top: -250px;
      width:400px;
      height:500px;
      box-shadow: @box-shadow;
      overflow: hidden;
      .bm-ea-content{
        position: absolute;
        z-index: 2;
        top:0;
        left:0;
        width:100%;
        height:100%;
        background:rgba(255, 255, 255, .7);
        .preview-box{
          width: 100%;
          height: 120px;
          margin-top: 20px;
        }
        .bm-ea-set-detail{
          padding: 0 50px;
          .bm-ea-detail-box{
            font-weight: bold;
            height:40px;
            display: flex;
            align-items: center;
            .name{
              font-size: 20px;
              color: @main-txt-color;
              font-weight: bold;
              user-select: none;
            }
            .input{
              flex-grow: 1;
              margin-left: 8px;;
              width: 200px;
              font-size: 20px;
              border-top: none;
              border-left: none;
              border-right: none;
              background:transparent;
              transition: @animateTime;
              &[type="text"]{
                color: @main-txt-color;
                border-bottom: 1px solid rgba(255, 255, 255, 0.6);
              }
              &:focus{
                outline: none;
                color: #000;
                border-color: @main-color;
              }
              .color-item{
                width: 20px;
                height: 20px;
                float: left;
                margin: 2px;
                background: red;
                cursor: pointer;
                transition: @animateTime;
                &.active{
                  transform: scale(.8);
                  &::after{
                    content: "";
                    position: absolute;
                    width: 20px;
                    height: 20px;
                    transform: scale(1.25);
                    border: 1px solid #de1a1a;
                  }
                }
              }
            }
          }
          .icontype-box{
            position: absolute; 
            width:300px;
            height: 150px;
            // border: 1px solid rgba(255, 255, 255, 0.8);
            .image-box{
              position: relative;
              width: 120px;
              height:120px;
              border: 1px solid rgba(0, 0, 0, 0.2);
              cursor: pointer;
              overflow: hidden;
              border-radius: 50%;
              >img{
                width: 100%;
              }
              .input-img{
                position: absolute;
                z-index: 10;
                top:0;
                left:0;
                width:100%;
                height:100%;
                // border-radius: 50%;
                transition: @animateTime;
                background: rgba(0, 0, 0, 0.3);
                font-size: 32px;
                color:#fff;
                opacity: 0;
                &:hover{
                  opacity: 1;
                }
              }
            }
          }
          .txt-type-box{
            position: absolute;
          }
        }
        .bottom-ctrl{
          position:absolute;
          bottom:0;
          width:100%;
          height:60px;
          background:rgba(255, 255, 255, 0.6);
        }
      }
      .bm-ea-bg{
        position: absolute;
        z-index: 1;
        left: -50vw;
        top:-10vh;
        width:100vw;
        height:100vh;
        background-attachment: fixed;
        background-repeat: no-repeat;
        background-position: 0 0;
        background-size: 100%;
        filter: blur(30px);
      }
    }
  }
}
</style>
