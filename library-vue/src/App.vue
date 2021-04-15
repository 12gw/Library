<template>
  <div id="app">
    <router-view/>
  </div>
</template>
<script>

  import NavMenu from '@/components/common/NavMenu'

  export default {
    name: 'App',
    components: {NavMenu}
  }
</script>

<style>
  /*引入inconfont*/
  @import url('//at.alicdn.com/t/font_2487650_x8k1k7g8hqf.css');
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    /*margin-top: 10px;*/
  }
</style>
