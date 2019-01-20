<template>
  <!-- 三相开关 -->
  <div class="switch-three">
    <div class="option active mid-center">显</div>
    <div class="option mid-center">隐</div>
    <div class="option mid-center">关</div>
  </div>
</template>

<script>
export default {
  name: "swicthThree",
  props: ["switchChecked", "switchName", "switchId", "changeData"],
  data(){
    return {
    }
  },
  methods: {
  }
};
</script>

<style lang="less">
@import "../assets/css/common.less";
  // 三相开关
  .switch-three{
    width:100px;
    height:24px;
    display: flex;
    border-radius: 12px;
    overflow: hidden;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.05) inset;
    font-size:12px;
    font-weight: bold;
    .option{
      height: 100%;
      flex: 1 0 auto;
      cursor:pointer;
      transition: @animateTime;
      &:not(:first-child){
        border-left: 1px solid rgb(233, 233, 233);
      }
      &:hover{
        background: #ececec;
      }
      &.active{
        background:@main-green;
        box-shadow: 0 0 15px rgba(0, 0, 0, 0.2) inset;
        color: #fff;
      }
    }
  }
</style>

