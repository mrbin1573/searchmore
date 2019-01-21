<template>
  <transition name="to-top" tag="div">
    <div>
      <draggable 
        v-model="dataCopy" 
        @start="drag=true" 
        @end="dragEnd"
        :options="{
          group:'item',
          animation: 500,
        }"
      >
        <a
          class="sub-menu-item"
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
      </draggable>
      <!-- 添加 -->
      <div
        class="sub-menu-item"
        target="_blank"
        href="javascript:;"
        @click.prevent="addBookMark"
      >
        <div class="icon mid-center">
          <span class="txt icon-add"></span>
        </div>
        <span class="name">添加</span>
      </div>
    </div>
  </transition>
</template>

<script>
import draggable from 'vuedraggable'

export default {
  name: "bookMark",
  props: ["bookMarkArr"],
  components: {
    draggable
  },
  data(){
    return {
      editShow: false,
      colorArr: [
        '#663ab6',
        '#00bbd3',
        '#f34236',
        '#9b27af',
        '#2195f2',
        '#009587',
        '#4bae4f',
        '#fe9700',
        '#fe5622',
        '#3e50b4',
        '#ccdb39',
        '#fe5622',
        '#fec007',
        '#03a8f3',
        '#8ac249',
        '#5f7c8a',
      ],
      dataCopy: this.bookMarkArr
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
      this.$emit();
      console.log(this.dataCopy)
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
      width:60%;
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

