<template>
  <div>
    <el-row style="margin: 18px 0px 20px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">管理中心</el-breadcrumb-item>
        <el-breadcrumb-item>学生系统</el-breadcrumb-item>
        <el-breadcrumb-item>借阅图书</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <book-borrow @onSubmit="loadBooks(current,size,null,null)" ref="edit"></book-borrow>
    <el-form ref="form" label-width="100px">
      <el-form-item label="书名或作者名">
        <el-input
          type="text"
          placeholder="请输入需要搜索的书名或作者名"
          v-model="keywords"
          size="medium">
        </el-input>
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="cid" clearable placeholder="请选择" style="float: left">
          <el-option
            v-for="item in options"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click.native.prevent="loadBooks(1,size,cid,keywords)">搜索</el-button>
        <el-button @click="handleReset()">重置</el-button>
      </el-form-item>
    </el-form>
    <el-card style="margin: 18px 2%;width: 95%">
      <div style="float: right">
        <el-button
          round
          @click.native.prevent="loadBooks(current,size,cid,keywords)"
          icon="el-icon-refresh"
          type="text"
        >刷新
        </el-button>
      </div>
      <el-table
        :data="books"
        stripe
        style="width: 100%"
        :max-height="tableHeight">
        <el-table-column
          prop="id"
          label="书籍编号"
          fit>
        </el-table-column>
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline>
              <el-form-item>
                <span>{{ props.row.abs }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          prop="title"
          label="书名（展开查看摘要）"
          fit>
        </el-table-column>
        <el-table-column
          prop="categoryName"
          label="分类"
          width="100">
        </el-table-column>
        <el-table-column
          prop="author"
          label="作者"
          fit>
        </el-table-column>
        <el-table-column
          prop="date"
          label="出版日期"
          width="120">
        </el-table-column>
        <el-table-column
          prop="press"
          label="出版社"
          fit>
        </el-table-column>
        <el-table-column
          prop="sum"
          label="总库存数"
          fit>
        </el-table-column>
        <el-table-column
          prop="stock"
          label="在库数量"
          fit>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          fit>
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="borrowBook(scope.row)"
              type="text"
              size="small">
              立即借阅
            </el-button>
            <el-button
              @click.native.prevent="preBorrowBook(scope.row)"
              type="text"
              size="small">
              预约借阅
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin: 20px 0 50px 0">
        <el-pagination
          background
          style="float:right;"
          layout="sizes, total, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="current"
          :page-sizes="[5, 10, 20]"
          :page-size="size"
          :total="total">
        </el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
  import BookBorrow from './BookBorrow'
  export default {
    components: {BookBorrow},
    name: 'StudentBookManagement',
    data () {
      return {
        current: 1,
        size: 5,
        total: 0,
        books: [],
        options: [],
        keywords: null,
        cid: null
      }
    },
    mounted () {
      this.loadBooks(this.current, this.size, null, null)
      this.loadOptions()
    },
    computed: {
      tableHeight () {
        return window.innerHeight - 320
      }
    },
    methods: {
      // 初始页currentPage、初始每页数据数pagesize和数据data
      handleSizeChange: function (val) {
        this.size = val
        this.current = 1
        this.loadBooks(this.current, this.size, this.cid, this.keywords)
      },
      handleCurrentChange: function (val) {
        this.current = val
        this.loadBooks(this.current, this.size, this.cid, this.keywords)
      },
      borrowBook (item) {
        this.$axios.post('admin/saveBorrow', {
          type: '0',
          bookname: item.title,
          bookid: item.id
        }).then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'info',
              message: '借阅成功！请在两个月之内还书，预期按每天一元计算'
            })
            this.loadBooks(1, this.size, null, null)
          }
        })
      },
      preBorrowBook (item) {
        this.$refs.edit.dialogFormVisible = true
        this.$refs.edit.form = {
          id: item.id,
          title: item.title,
          type: '1'
        }
      },
      loadOptions () {
        var _this = this
        this.$axios.get('admin/content/categories').then(resp => {
          if (resp && resp.data.code === 200) {
            _this.options = resp.data.result
          }
        })
      },
      loadBooks (current, size, cid, keywords) {
        var _this = this
        this.$axios.get('/bookLists', {
          params: {
            current: current,
            size: size,
            cid: cid,
            keywords: keywords
          }
        }).then(resp => {
          if (resp && resp.data.code === 200) {
            _this.books = resp.data.result.records
            _this.total = resp.data.result.total
          }
        })
      },
      handleReset () {
        this.cid = null
        this.keywords = null
      }
    }
  }
</script>

<style scoped>
</style>
