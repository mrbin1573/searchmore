<template>
  <!-- 红色背景 -->
  <div v-if="btnType == 'red'" class="btn mid-center red" @click="clickBtn"><slot></slot></div>
  <!-- 绿色背景 -->
  <div v-else-if="btnType == 'green'" class="btn mid-center green" @click="clickBtn"><slot></slot></div>
  <!-- 蓝色背景 -->
  <div v-else-if="btnType == 'blue'" class="btn mid-center blue" @click="clickBtn"><slot></slot></div>
</template>

<script>
export default {
  name: "myButton",
  props: ["btnType"],
  data(){
    return {
    }
  },
  methods: {
    clickBtn: function () {
      this.$emit('myBtnFun');
    }
  }
};
</script>

<style lang="less">
@import "../assets/css/common.less";
.btn{
  min-width:100px;
  height:40px;
  border-radius: 20px;
  color:#fff;
  padding: 0 20px;
  font-weight: bold;
  user-select: none;
  box-shadow: @box-shadow;
  transition: @animateTime;
  cursor: pointer;
  &.blue{
    background: @main-blue;
    &:hover{
      background: @main-blue-dark;
    }
  }
  &.green{
    background: @main-green;
    &:hover{
      background: @main-green-dark;
    }
  }
  &.red{
    background: @main-color;
    &:hover{
      background: @main-color-dark;
    }
  }
  &:active{
    transform: scale(.95);
  }
}
</style>

