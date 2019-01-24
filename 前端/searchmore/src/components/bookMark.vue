<template>
  <div v-if="!single">
    <draggable 
      v-model="dataCopy" 
      @start="drag=true" 
      @end="dragEnd"
      :options="{
        group:'item',
        animation: 500,
        filter: '.filtered',
      }"
    >
      <a
        class="sub-menu-item"
        :class="{small: small}"
        target="_blank"
        v-for="(bmItem, index) in dataCopy"
        :href="bmItem.url"
        @click.prevent.right="editBookMarkItem"
      >
        <div class="icon mid-center" :style="bmItem.style">
        <!-- <div class="icon mid-center"> -->
          <!-- 图标 -->
          <span class="svg" v-if="bmItem.iconType == 'svg'" :class="bmItem.icon"></span>
          <!-- 文字 -->
          <span class="txt" v-else-if="bmItem.iconType == 'text'">{{bmItem.icon}}</span>
          <!-- 图片 -->
          <img class="image" v-else-if="bmItem.iconType == 'image'" :src="bmItem.icon">
        </div>
        <span class="name">{{bmItem.name}}</span>
        <div class="del-bmark mid-center" v-show="editShow" @click.prevent="deleNow">
          <i class="icon-cha"></i>
        </div>
      </a>
      <!-- 添加 -->
      <div
        class="sub-menu-item filtered"
        target="_blank"
        href="javascript:;"
        @click.prevent="addBookMark"
      >
        <div class="icon mid-center">
          <span class="txt icon-add"></span>
        </div>
        <span class="name">添加</span>
      </div>
    </draggable>
  </div>
  <div v-else>
    <div class="sub-menu-item" >
      <div class="icon mid-center" :style="bookMarkArr.style">
      <!-- <div class="icon mid-center"> -->
        <!-- 图标 -->
        <span class="svg" v-if="bookMarkArr.iconType == 'svg'" :class="bookMarkArr.icon"></span>
        <!-- 文字 -->
        <span class="txt" v-else-if="bookMarkArr.iconType == 'text'">{{bookMarkArr.icon}}</span>
        <!-- 图片 -->
        <img class="image" v-else-if="bookMarkArr.iconType == 'image'" :src="bookMarkArr.icon">
      </div>
      <span class="name">{{bookMarkArr.name}}</span>
    </div>
  </div>

</template>

<script>
import draggable from 'vuedraggable'

export default {
  name: "bookMark",
  props: ["bookMarkArr", "dataIndex", "small", "single"],
  components: {
    draggable
  },
  data(){
    return {
      editShow: false,
      dataCopy: this.bookMarkArr,
    }
  },
  methods: {
    // 右击显示删除
    editBookMarkItem () {
      this.editShow = true;
      // alert('右击')
    },
    deleNow () {
      alert('删除')
    },
    addBookMark () {
      alert('添加')
    },
    // 拖动结束
    dragEnd () {
      // 传回数据到父级
      this.$emit('dragEnd', {data: this.dataCopy, index:this.dataIndex});
    }
  },
  watch: {
    bookMarkArr: {
      handler(newValue, oldValue) {
        this.dataCopy = newValue;
        console.log(newValue);
      },
      deep: true
  　}
  }
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
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: @animateTime;
  user-select: none;
  text-decoration: none;
  text-align: center;
  &.small{
    width:70px;
    height:70px;
    .icon {
      width: 50px;
      height: 50px;
    }
  }
  .icon {
    width: 65px;
    height: 65px;
    border-radius: 50%;
    background: rgba(0, 0, 0, 0.322);
    overflow: hidden;
    .svg{
      color: #fff;
      font-size: 30px;
    }
    .txt {
      color: #fff;
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
  &:hover {
    background: rgba(0, 0, 0, 0.507);
    box-shadow: @box-shadow;
    // border: 2px solid rgba(0, 0, 0, 0.13);
  }
}
</style>

