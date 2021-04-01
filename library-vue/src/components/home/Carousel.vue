<template>
  <el-card class="card-carousel">
  <el-carousel  class="push" :interval="4000" arrow="always">
    <el-carousel-item v-for="item in items" :key="item.id">
      <a :href=item.link target="_blank">
        <img :src=item.img alt="" class="carousel_img">
      </a>
      <h3>{{item.title}}</h3>
    </el-carousel-item>
  </el-carousel>
  </el-card>
</template>

<style>
  .el-carousel__item h3 {
    color: black;
    font-size: 15px;
    /*line-height: 300px;*/
    bottom: 43px;
    float: left;
    position: relative;
  }

  .card-carousel {
    /*margin: 0 auto;*/
    margin-left: 20px;
    width: 680px;
    height: 320px;
  }

  .push {
    /*margin-left: -15px;*/
  }

  .el-carousel__item:nth-child(2n) {
    background: black;
  }

  .el-carousel__item:nth-child(2n+1) {
    background: black;
  }

  .carousel_img {
    height: 100%;
    width: 100%;
    text-align: center;
  }
</style>

<script>
  export default {
    name: 'Carousel',
    data: function () {
      return {
        items: [{
          id: 1,
          title: '与病对话-胡冰霜',
          img: 'https://i.loli.net/2019/04/10/5cada962c287c.jpg',
          link: ''},
        {
          id: 2,
          title: '上帝笑了99次-[英]彼得·凯弗',
          img: 'https://i.loli.net/2019/04/10/5cada858e6019.jpg',
          link: ''
        },
        {
          id: 3,
          title: '互联网算法-[美] 菲斯曼等',
          img: 'https://i.loli.net/2019/04/10/5cada8e1aa892.jpg',
          link: ''
        }]
      }
    }
  }
</script>
