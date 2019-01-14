<template>
  <transition name="opacityShow">
    <div class="alert-box" v-show="alertObj.show">
      <transition name="to-top">
        <div class="alert-content" v-show="alertObj.show">
          <div class="title">
            sexy coder online tips
          </div>
          <div class="content">
            <i class="icon icon-jinggao"></i>&nbsp;
            {{alertObj.text}}
            <span class="txt-en">{{alertObj.textEn}}</span>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'bookMark',
  props:['alertObj'],
  data () {
    return {
    }
  },
}
</script>

<style lang="less">
  @import '../assets/css/common.less';
  .alert-box{
    position: fixed;
    width: 100%;
    min-height:100%;
    z-index: 100;
    .alert-content{
      margin: 10% auto 0;
      display: flex;
      flex-direction: column;
      width: 400px;
      user-select: none;
      background:rgb(255, 255, 255);
      box-shadow: @box-shadow;
      cursor: pointer;
      .title{
        display: flex;
        justify-content: center;
        align-items: center;
        background: @main-txt-color;
        font-size: 18px;
        color: #fff;
        font-weight: bold;
        width:100%;
        height: 50px;
      }
      .content{
        color: @main-color;
        display: flex;
        flex-direction: column;
        justify-content: center;
        padding: 30px 20px 40px;
        min-height:150px;
        align-items: center;
        font-size: 24px;
        font-weight: bold;
        .icon{
          margin-bottom: 10px;
          font-size: 50px;
        }
        .txt-en{
          font-size: 16px;
          font-weight: normal;
        }
      }
    }
  }
</style>
