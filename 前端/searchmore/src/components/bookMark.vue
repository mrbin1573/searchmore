<template>
  <div class="sub-menu-item"
    @click.prevent.right="editBookMarkShow"
  >
    <!-- 内容 -->
    <a class="bmitem-content" target="_blank" :href="bookMarkData.url">
      <div class="icon mid-center" :style="JSON.parse(bookMarkData.style)">
        <!-- 图标 -->
        <span class="svg" v-if="bookMarkData.iconType == 'svg'" :class="bookMarkData.icon"></span>
        <!-- 文字 -->
        <span class="txt" v-else-if="bookMarkData.iconType == 'text'">{{bookMarkData.icon}}</span>
        <!-- 图片 -->
        <img class="image" v-else-if="bookMarkData.iconType == 'image'" :src="bookMarkData.icon" :alt="bookMarkData.name">
      </div>
      <!-- 名字 -->
      <span class="name">{{bookMarkData.name}}</span>
    </a>

    <!-- 操作 -->
    <transition name="opacityShow">
      <div class="mookmark-ctrl-box" v-show="editShow" v-if="edit != false">
        <transition name="to-right">
          <div class="bookmark-ctrl-item mid-center icon-yumaobi" v-show="editShow" @click="editBookMark"></div>
        </transition>
        <transition name="to-left">
          <div class="bookmark-ctrl-item mid-center icon-shanchu" v-show="editShow" @click="deleBookMark"></div>
        </transition>
      </div>
    </transition>
    <!-- 背景 -->
    <div class="bg" v-if="bgObj" :style="{backgroundImage: 'url(' +bgObj.src+ ')'}"></div>
  </div>
</template>

<script>
export default {
  name: "bookMark",
  props: ["bookMarkData", "indexParent", "indexChild", "bgObj", "edit"],
  data(){
    return {
      editShow: false,
    }
  },
  methods: {
    // 右击显示删除
    editBookMarkShow () {
      this.editShow = true;
    },
    
    // 编辑
    editBookMark () {
      var resObj = {
        'type':'edit',
        'indexParent': this.indexParent,
        'indexChild': this.indexChild
      }
      this.$emit('bookMarkCallBack', resObj);
      this.editShow = false;
    },

    // 删除
    deleBookMark () {
      var resObj = {
        'type':'delete',
        'indexParent': this.indexParent,
        'indexChild': this.indexChild
      }
      this.$emit('bookMarkCallBack', resObj);
      this.editShow = false;
    },
  },
};
</script>

<style lang="less">
@import "../assets/css/common.less";
.sub-menu-item {
  position: relative;
  width: 100px;
  height: 100px;
  margin: 4px;
  float: left;
  background: rgba(0, 0, 0, 0.3);
  cursor: pointer;
  transition: @animateTime;
  user-select: none;
  text-decoration: none;
  text-align: center;
  overflow: hidden;
  .bmitem-content{
    position: absolute;
    width:100%;
    height:100%;
    z-index: 10;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-decoration: none;
    .icon {
      width: 65px;
      height: 65px;
      border-radius: 50%;
      background: rgba(0, 0, 0, 0.322);
      overflow: hidden;
      color: #fff;
      .svg{
        color: #fff;
        font-size: 30px;
      }
      .txt {
      }
      .image{
        width:100%;
      }
    }
    .name {
      color: #fff;
      font-size: 12px;
      margin-top: 5px;
    }
    .del-bmark{
      position: absolute;
      top:0;
      right:0;
      width:30px;
      height: 30px;
      color: @main-color;
      background: rgba(0, 0, 0, 0.5);
      font-size: 12px;
    }
  }
  &:active {
    transform: scale(0.98);
  }
  &:hover {
    background: rgba(0, 0, 0, 0.767);
    box-shadow: @box-shadow;
    .bg{
      filter: blur(20px);
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
    opacity: .7;
    transition: @animateTime;
    filter: blur(30px);
  }
  .mookmark-ctrl-box{
    position: absolute;
    width:100%;
    height:100%;
    z-index: 20;
    background: rgba(105, 105, 105, 0.9);
    display: flex;
    align-items: center;
    transition: @animateTime;
    .bookmark-ctrl-item{
      width:50%;
      height:50%;
      background: rgba(255, 255, 255, 0.8);
      &:hover{
        background: rgba(226, 226, 226, 0.8);
      }
      &.icon-yumaobi{
        color: @main-blue;
      }
      &.icon-shanchu{
        color: @main-color;
      }
      &:first-child{
        border-right: 1px solid #f1f1f1;
      }
    }
  }
}
</style>

