<template>
  <a class="sub-menu-item" 
    target="_blank"
    :href="bookMarkData.url"
    @click.prevent.right="editBookMarkItem"
  >
    <div class="bmitem-content">
      <div class="icon mid-center" :style="JSON.parse(bookMarkData.style)">
        <!-- 图标 -->
        <span class="svg" v-if="bookMarkData.iconType == 'svg'" :class="bookMarkData.icon"></span>
        <!-- 文字 -->
        <span class="txt" v-else-if="bookMarkData.iconType == 'text'">{{bookMarkData.icon}}</span>
        <!-- 图片 -->
        <img class="image" v-else-if="bookMarkData.iconType == 'image'" :src="bookMarkData.icon" :alt="bookMarkData.name">
      </div>
      <span class="name">{{bookMarkData.name}}</span>
    </div>
    <div class="bg" v-if="bgObj" :style="{backgroundImage: 'url(' +bgObj.src+ ')'}"></div>
  </a>
</template>

<script>
export default {
  name: "bookMark",
  props: ["bookMarkData", "bgObj"],
  data(){
    return {
      editShow: false,
    }
  },
  methods: {
    // 右击显示删除
    editBookMarkItem () {
      this.editShow = true;
    },
    deleNow () {
      alert('删除')
    },
    addBookMark () {
      alert('添加')
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
    &:active {
      transform: scale(0.9);
    }
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
}
</style>

