<template>
  <!-- 三相开关 -->
  <div class="switch-three">
    <div class="option active mid-center" @click="selectThis('left')"><slot name="val-one">0</slot></div>
    <div class="option mid-center"        @click="selectThis('center')"><slot name="val-two">1</slot></div>
    <div class="option mid-center"        @click="selectThis('right')"><slot name="val-three">2</slot></div>
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
    selectThis: function( flag ) {
      this.$emit('selectSwitchThree', flag);
    }
  }
};
</script>

<style lang="less">
@import "../assets/css/common.less";
  // 三相开关
  .switch-three{
    min-width:100px;
    height:24px;
    display: flex;
    border-radius: 12px;
    overflow: hidden;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.05) inset;
    background: rgba(255, 255, 255, 0.5);
    font-size:12px;
    font-weight: bold;
    .option{
      height: 100%;
      flex: 1 0 auto;
      padding: 0 8px;
      cursor:pointer;
      transition: @animateTime;
      &:not(:first-child){
        border-left: 1px solid rgba(0, 0, 0, 0.1);
      }
      &:hover{
        background: #d1d1d1;
      }
      &.active{
        background:@main-green;
        box-shadow: 0 0 15px rgba(0, 0, 0, 0.2) inset;
        color: #fff;
      }
    }
  }
</style>

