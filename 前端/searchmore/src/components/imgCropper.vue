<template>
  <!-- 图片裁剪 -->
  <transition name="opacityShow">
    <div class="my-img-cropper-box mid-center" v-show="imgCropperShow">
      <transition name="to-top">
        <div class="cropper-box" v-show="imgCropperShow">
          <!-- 截图框 -->
          <div class="img-box">
            <vueCropper
              ref="cropper"
              :img="cropperOpts.img"
              :outputType="cropperOpts.outputType"
              :autoCrop="cropperOpts.autoCrop"
              :autoCropWidth="cropperOpts.autoCropWidth"
              :autoCropHeight="cropperOpts.autoCropWidth"
              :info="cropperOpts.info"
              :fixedBox="cropperOpts.fixedBox"
              :canMoveBox="cropperOpts.canMoveBox"
              :centerBox="cropperOpts.centerBox"
              @realTime="realTime"
            ></vueCropper>
          </div>

          <!-- 预览 -->
          <div class="preview mid-center">
            <div class="show-preview">
              <div :style="cropperPreview.div">
                <img :src="cropperOpts.img" :style="cropperPreview.img">
              </div>
            </div>
          </div>

          <!-- 操作剪切 -->
          <div class="cropper-ctrl">
            <myButton @myBtnFun="">
              <input class="re-select-img" type="file" name="" id="" accept=".jpg,.png">
              <div class="name mid-center">重选</div>
            </myButton>
            <myButton @myBtnFun="rotateRight" class="ml5"><i class="icon-spinner11"></i>&nbsp;90°</myButton>
            <myButton @myBtnFun="rotateRight" class="ml5">重置</myButton>
          </div>

          <!-- 背景颜色 -->
          <div class="cropper-img-bgcolor">
            <h4 class="cropper-img-bgcolor-title">背景颜色</h4>
            <div class="color-list-box">
              <!-- 预设颜色 -->
              <div class="color-item mid-center" 
              v-for="(colorItem, index) in colorArr" 
              :class="{active: index == colorActiveIndex}"
              @click="selectColor (index)"
              :style="{backgroundColor:colorItem}">{{index + 1}}</div>
              <!-- 取色 -->
              <input class="color-item mid-center select-color"  type="color" name="" id="">
            </div>
          </div>
          <!-- 确认 取消 操作按钮 -->
          <div class="cropper-bottom mid-center">
            <myButton :btnType="'red'" @myBtnFun="imgCropperShow = false">取消裁剪</myButton>
            <br>
            <myButton :btnType="'green'" @myBtnFun="" class="ml20">完成裁剪</myButton>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>

<script>
// import { vueCropper }   from 'vue-cropper' 
import myButton         from './myButton.vue'

export default {
  name: "imgCropper",
  components: {
    // vueCropper,     // 图片裁剪
    myButton,       // 圆角按钮
  },
  props: ["imgCropperShow"],
  data(){
    return {
      // 图片裁剪配置
      cropperOpts: {
        img: 'http://hackbinimg.luokangyuan.com/searchmore/bg1.jpg',
        outputType: 'png',
        autoCrop: true,          // 是否默认生成截图框
        autoCropWidth: 200,
        autoCropHeight: 200,
        info: false,             // 裁剪框的大小信息
        fixedBox: true,
        canMoveBox: false,       // 截图框能否拖动
        centerBox: false,         // 截图框是否被限制在图片里面
      },
      cropperPreview: {},        // 预览
      
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
        'rgba(0, 0, 0, 0.3)',
      ],
      // 被选中颜色
      colorActiveIndex: -1,
    }
  },
  methods: {
    clickBtn: function () {
      this.$emit('myBtnFun');
    },
    
    // =============图片裁剪====================
    // 实时预览
    realTime: function (data) {
      this.cropperPreview = data;
    },

    // 右旋转90
    rotateRight: function () {
      this.$refs.cropper.rotateRight() 
    },
    
    // 选择颜色
    selectColor: function (index) {
      console.log(this.colorArr[index]);
      this.colorActiveIndex = index;
    },

  }
};
</script>

<style lang="less">
@import "../assets/css/common.less";
.my-img-cropper-box{
  position: fixed;
  z-index: @zImgCropper;
  top:0;
  left:0;
  width: 100%;
  height:100%;
  background: rgba(0, 0, 0, 0.3);
  .cropper-box{
    position: relative;
    width: 580px;
    height:500px;
    background: #fff;
    box-shadow: @box-shadow;
    padding: 30px;
    .img-box{
      width: 300px;
      height:300px;
      overflow: hidden;
      .vue-cropper {
        .cropper-crop-box{
          border-radius: 50%;
          overflow: hidden;
          border: 1px solid #39f; 
        }
      }
    }
    .preview{
      position:absolute;
      left:350px;
      top:30px;
      width: 198px;
      height:198px;
      background: rgba(0, 0, 0, 0.3);
      .show-preview{
        width:150px;
        height:150px;
        border-radius: 50%;
        overflow: hidden;
      }
    }
    .cropper-ctrl{
      position:absolute;
      top:290px;
      left:350px;
      width: 200px;
      display: flex;
      flex-wrap: wrap;
      .btn{
        box-shadow: 0 0 0 transparent;
        position: relative;
        .re-select-img{
          position: absolute;
          z-index: 20;
          width:100%;
          height:100%;
          opacity: 0;
          cursor: pointer;
        }
        .name{
          position: absolute;
          z-index: 1;
          width: 100%;
          height:100%;
          cursor: pointer;
        }
      }
    }
    .cropper-img-bgcolor{
      position: absolute;
      top: 340px;
      .cropper-img-bgcolor-title{
        color: @main-txt-color;
      }
      .color-list-box{
        margin-top: 10px;
        display: flex;
        flex-wrap: wrap;
        .color-item{
          width: 30px;
          height: 30px;
          margin-right: 5px;
          color: #fff;
          user-select: none;
          cursor: pointer;
          transition:@animateTime;
          &.active{
            transform: scale(.8);
            &::after{
              content:"";
              position: absolute;
              width: 30px;
              height:30px;
              transform: scale(1.25);
              border: 1px solid @main-color;
            }
          }
          &:active{
            transform: scale(.85);
          }
        }
      }
    }
    .cropper-bottom{
      position:absolute;
      bottom:0;
      left:0;
      width: 100%;
      height:60px;
      background: #ebebeb;
      justify-content: flex-end;
      padding:0 30px;
    }
  }
}
</style>

