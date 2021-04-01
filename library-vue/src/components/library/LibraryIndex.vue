<template>
  <el-container>
    <el-aside style="width: 200px;margin-top: 20px">
      <switch></switch>
      <SideMenu @indexSelect="listByCategory" ref="sideMenu"></SideMenu>
    </el-aside>
    <el-main>
      <books class="books-area" ref="booksArea"></books>
    </el-main>
  </el-container>
</template>

<script>
  import SideMenu from './SideMenu'
  import Books from './Books'

  export default {
    name: 'AppLibrary',
    components: {Books, SideMenu},
    data () {
      return {
        cid: 0
      }
    },
    methods: {
      listByCategory () {
        var _this = this
        var url = 'categories/books'
        this.$axios.get(url, {
          params: {
              cid: this.$refs.sideMenu.cid
            }
        }).then(resp => {
          if (resp && resp.data.code === 200) {
            _this.$refs.booksArea.books = resp.data.result
            _this.$refs.booksArea.currentPage = 1
          }
        })
      }
    }
  }
</script>

<style scoped>
  .books-area {
    width: 990px;
    margin-left: auto;
    margin-right: auto;
  }
</style>
