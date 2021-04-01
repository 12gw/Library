<template>
  <div>
    <el-row style="margin: 18px 0px 10px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">管理中心</el-breadcrumb-item>
        <el-breadcrumb-item>学生系统</el-breadcrumb-item>
        <el-breadcrumb-item>借阅记录</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <el-form ref="form" label-width="100px">
      <el-form-item label="书名">
        <el-input
          type="text"
          placeholder="请输入需要搜索的书名"
          v-model="bookname">
        </el-input>
      </el-form-item>
      <el-form-item label="借阅类型">
        <el-select v-model="value" clearable placeholder="请选择" style="float: left">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="借阅状态">
        <el-select v-model="status" clearable placeholder="请选择" style="float: left">
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click.native.prevent="select(value,status,bookname)">搜索</el-button>
        <el-button @click="handleReset()">重置</el-button>
      </el-form-item>
    </el-form>
    <el-card style="margin: 18px 2%;width: 95%">
      <div style="float: right">
        <el-button
          round
          @click.native.prevent="loadBorrows()"
          icon="el-icon-refresh"
          type="text"
        >刷新
        </el-button>
      </div>
      <el-table
        :data="borrows"
        stripe
        style="width: 100%"
        :max-height="tableHeight">
        <el-table-column
          prop="bookid"
          label="书籍编号"
          width="110">
        </el-table-column>
        <el-table-column
          prop="bookname"
          label="书名"
          fit>
        </el-table-column>
        <el-table-column
          prop="username"
          label="借阅者"
          fit>
        </el-table-column>
        <el-table-column
          prop="type"
          label="借阅类型"
          fit>
        </el-table-column>
        <el-table-column
          prop="borrowTime"
          label="借阅时间"
          fit>
        </el-table-column>
        <el-table-column
          prop="returnTime"
          label="还书时间"
          fit>
        </el-table-column>
        <el-table-column
          prop="money"
          label="逾期罚款"
          fit>
        </el-table-column>
        <el-table-column
          prop="status"
          label="借阅状态"
          fit>
          <template slot-scope="scope">
            <p v-if="scope.row.status==0">已还书</p>
            <p v-if="scope.row.status==1">未还书</p>
            <p v-if="scope.row.status==2">已预约</p>
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
  export default {
    name: 'StudentBorrowManagement',
    data () {
      return {
        bookname: null,
        current: 1,
        size: 10,
        total: 0,
        borrows: [],
        options: [{
          value: '1',
          label: '预约借书'
        }, {
          value: '0',
          label: '普通借书'
        }],
        value: null,
        statusOptions: [{
          value: '1',
          label: '未还书'
        }, {
          value: '0',
          label: '已还书'
        }, {
          value: '2',
          label: '已预约'
        }],
        status: null
      }
    },
    mounted () {
      this.loadBorrows()
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
        this.loadBorrows()
      },
      handleCurrentChange: function (val) {
        this.current = val
        this.loadBorrows()
      },
      handleReset () {
        this.value = null
        this.bookname = null
        this.status = null
      },
      select (value, status, bookname) {
        var _this = this
        this.$axios.get('/getBorrowList', {
          params: {
            current: _this.current,
            size: _this.size,
            type: value,
            status: status,
            bookname: bookname
          }
        }).then(resp => {
          if (resp && resp.data.code === 200) {
            _this.borrows = resp.data.result.records
            _this.total = resp.data.result.total
          }
        })
      },
      loadBorrows () {
        var _this = this
        this.$axios.get('/getBorrowList', {
          params: {
            current: _this.current,
            size: _this.size
          }
        }).then(resp => {
          if (resp && resp.data.code === 200) {
            _this.borrows = resp.data.result.records
            _this.total = resp.data.result.total
          }
        })
      }
    }
  }
</script>

<style scoped>
</style>
