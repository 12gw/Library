<template>
  <div>
    <el-row style="margin: 18px 5px 10px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">管理中心</el-breadcrumb-item>
        <el-breadcrumb-item>学生系统</el-breadcrumb-item>
        <el-breadcrumb-item>借阅图书</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <el-form ref="form" label-width="100px">
      <el-form-item label="内容">
        <el-input
          type="textarea"
          autosize
          placeholder="请输入内容"
          v-model="content">
        </el-input>
      </el-form-item>
      <el-form-item label="留言类型">
        <el-select v-model="type" clearable placeholder="请选择" style="float: left">
          <el-option
            v-for="item in typeoptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="留言状态">
        <el-select v-model="status" clearable placeholder="请选择" style="float: left">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="select(content,status,type)">搜索</el-button>
        <el-button @click="handleReset()">重置</el-button>
      </el-form-item>
    </el-form>
    <message-board @onSubmit="loadMessage(1, size, content , status ,type)" ref="message"></message-board>
    <el-card style="margin: 18px 2%;width: 95%">
      <div style="float: right">
        <el-button
          round
          @click.native.prevent="loadMessage(current, size, content , status ,type)"
          icon="el-icon-refresh"
          type="text">
          刷新
        </el-button>
      </div>
      <el-table
        stripe
        :data="mydata"
        style="width: 100%"
        :max-height="tableHeight">
        <el-table-column
          label="编号"
          prop="id"
          width="55">
        </el-table-column>
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-form label-position="left" inline>
              <el-form-item>
                <span>{{ scope.row.message}}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          prop="username"
          label="用户名"
          fit>
        </el-table-column>
        <el-table-column
          prop="message"
          label="内容"
          :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column
          prop="type"
          label="留言类型"
          fit>
          <template slot-scope="scope">
            <p v-if="scope.row.type === '1'">建议</p>
            <p v-if="scope.row.type === '2'">读后感</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="留言时间"
          fit>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          fit>
          <template slot-scope="scope">
            <p v-if="scope.row.status === '0'">正常</p>
            <p v-if="scope.row.status === '1'">已删除</p>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作">
          <template slot-scope="scope">
            <el-popover
              :ref="`popover-${scope.row.id}`"
              placement="top"
              width="160">
              <p>确定删除这条内容吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="scope._self.$refs[`popover-${scope.row.id}`].doClose()">
                  取消
                </el-button>
                <el-button type="primary" size="mini" @click="scope._self.$refs[`popover-${scope.row.id}`].doClose(), delMessage(scope.row)">
                  确定
                </el-button>
              </div>
              <el-button
                slot="reference"
                type="danger"
                size="small"
                :disabled="scope.row.status === '1'">
                删除
              </el-button>
            </el-popover>
            <el-button
              @click="editMessage(scope.row)"
              type="primary"
              size="small">
              编辑
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
  import MessageBoard from './MessageBoard'

  export default {
    components: {MessageBoard},
    name: 'ContentMessageManagement',
    data () {
      return {
        visible: false,
        content: null,
        current: 1,
        size: 5,
        total: 0,
        mydata: [],
        typeoptions: [{
          value: '1',
          label: '建议'
        }, {
          value: '2',
          label: '读后感'
        }],
        status: null,
        options: [{
          value: '0',
          label: '正常'
        }, {
          value: '1',
          label: '已删除'
        }],
        type: null
      }
    },
    computed: {
      tableHeight () {
        return window.innerHeight - 320
      }
    },
    mounted () {
      this.loadMessage(this.current, this.size, this.content, this.status, this.type)
    },
    methods: {
      select (content, status, type) {
        this.current = 1
        this.size = 5
        this.loadMessage(this.current, this.size, content, status, type)
      },
      delMessage (item) {
        let param = new URLSearchParams()
        param.append('status', '1')
        param.append('id', item.id)
        this.$axios.put('admin/updateMessage', param
        ).then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'info',
              message: resp.data.message
            })
            this.loadMessage(this.current, this.size, this.content, this.status, this.type)
          }
        })
      },
      handleReset () {
        this.status = null
        this.content = null
        this.type = null
      },
      // 初始页currentPage、初始每页数据数pagesize和数据data
      handleSizeChange: function (val) {
        this.size = val
        this.current = 1
        this.loadMessage(this.current, this.size, this.content, this.status, this.type)
      },
      handleCurrentChange: function (val) {
        this.current = val
        this.loadMessage(this.current, this.size, this.content, this.status, this.type)
      },
      editMessage (item) {
        this.$refs.message.dialogFormVisible = true
        this.$refs.message.form = {
          id: item.id,
          status: item.status,
          message: item.message,
          type: item.type
        }
      },
      loadMessage (current, size, content, status, type) {
        var _this = this
        this.$axios.get('/messageBoardList', {
          params: {
            current: current,
            size: size,
            message: content,
            status: status,
            type: type
          }
        }).then(resp => {
          if (resp && resp.data.code === 200) {
            _this.mydata = resp.data.result.records
            _this.total = resp.data.result.total
          }
        })
      }
    }
  }
</script>
