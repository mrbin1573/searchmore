<template>
  <div class="switch-obj">
    <input class="switch-btn" type="checkbox" :checked="switchChecked" :name="switchName" :id="switchId">
    <label :for="switchId" @click="selectClick" class="switch-btn-label"></label>
  </div>
</template>

<script>
export default {
  name: "swicth",
  props: ["switchChecked", "switchName", "switchId"],
  data(){
    return {
      editShow: false
    }
  },
  methods: {
    selectClick: function () {

    }
  }
};
</script>

<style lang="less">
@import "../assets/css/common.less";
// 开关
.switch-obj{
  width:45px;
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
</style>

