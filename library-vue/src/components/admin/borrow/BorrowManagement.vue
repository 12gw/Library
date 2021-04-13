<template>
  <div>
    <el-row style="margin: 18px 0px 10px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">管理中心</el-breadcrumb-item>
        <el-breadcrumb-item>借阅管理</el-breadcrumb-item>
        <el-breadcrumb-item>借阅管理</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <el-form ref="form" label-width="100px" >
      <el-form-item label="用户名">
        <el-input
          type="text"
          placeholder="请输入需要搜索的用户名"
          v-model="name">
        </el-input>
      </el-form-item>
      <el-form-item label="书名">
        <el-input
          type="text"
          placeholder="请输入需要搜索的书名"
          v-model="bookname">
        </el-input>
      </el-form-item>
      <el-form-item label="借阅类型" >
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
        <el-button @click.native.prevent="select(name,value,status,bookname)" icon="el-icon-search">搜索</el-button>
        <el-button @click="handleReset()" icon="el-icon-refresh-right">重置</el-button>
      </el-form-item>
    </el-form>
    <el-card style="margin: 18px 2%;width: 95%">
      <div style="float: right">
        <el-button
          round
          @click.native.prevent= "loadBorrows()"
          icon="el-icon-refresh"
          type="text"
        >刷新</el-button>
      </div>
      <el-table
        :data="borrows"
        stripe
        style="width: 100%"
        :max-height="tableHeight">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          prop="bookid"
          label="书籍编号"
          fit>
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
          <p v-if="scope.row.status==3">预约超时</p>
        </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          fit>
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="editBorrow(scope.row)"
              type="text"
              size="small"
              :disabled ="scope.row.status == 0 || scope.row.status == 3 " >
              还书
            </el-button>
            <el-button
              @click.native.prevent="deleteBorrow(scope.row)"
              type="text"
              size="small"
              :disabled ="scope.row.money == 0 || scope.row.status == 3">
              交逾期罚款
            </el-button>
            <el-popover :ref="`popover-${scope.row.id}`" placement="top" width="160">
              <p>确定取书？</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="scope._self.$refs[`popover-${scope.row.id}`].doClose()">
                  取消
                </el-button>
                <el-button type="primary" size="mini" @click="scope._self.$refs[`popover-${scope.row.id}`].doClose(), orderBorrow(scope.row)">
                  确定
                </el-button>
              </div>
              <el-button slot="reference" type="text" size="small" :disabled ="scope.row.status !== '2'">
                取书
              </el-button>
            </el-popover>
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
    name: 'BorrowManagement',
    data () {
      return {
        bookname: null,
        name: null,
        current: 1,
        size: 5,
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
        }, {
          value: '3',
          label: '预约超时'
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
        this.current = 1
        this.loadBorrows()
      },
      handleCurrentChange: function (val) {
        this.current = val
        this.loadBorrows()
      },
      handleReset () {
        this.value = null
        this.bookname = null
        this.name = null
        this.status = null
      },
      select (name, value, status, bookname) {
        var _this = this
        this.current = 1
        this.size = 5
        this.$axios.get('/getBorrowList', {
          params: {
            current: _this.current,
            size: _this.size,
            username: name,
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
      },
      deleteBorrow (item) {
        this.$confirm('是否缴清罚款, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            this.$axios.put('admin/borrow/updateBorrow', {
              username: item.username,
              // status: item.status,
              money: item.money,
              bookid: item.bookid,
              id: item.id
            }).then(resp => {
              if (resp && resp.data.code === 200) {
                this.$message({
                  type: 'info',
                  message: '缴费成功'
                })
                this.loadBorrows()
              }
            })
          }
        ).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消缴清罚款'
          })
        })
      },
      editBorrow (item) {
        this.$confirm('是否还书, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            this.$axios
              .put('admin/borrow/updateBorrow', {
                id: item.id,
                username: item.username,
                status: item.status,
                bookid: item.bookid
              }).then(resp => {
              if (resp && resp.data.code === 200) {
                this.$message({
                  type: 'info',
                  message: '还书成功'
                })
                this.loadBorrows()
              } else {
                this.$message({
                  type: 'info',
                  message: '还书失败'
                })
              }
            })
          }
        ).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消还书'
          })
        })
      },
      orderBorrow (item) {
        this.$axios
          .put('admin/borrow/updateBorrow', {
            id: item.id,
            username: item.username,
            status: item.status,
            bookid: item.bookid
          }).then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'info',
              message: '取书成功！'
            })
            this.loadBorrows()
          }
        })
      }
    }
  }
</script>

<style scoped>
</style>
