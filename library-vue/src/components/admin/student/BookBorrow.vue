<template>
  <div style="text-align: left">
    <el-dialog
      title="预约借书"
      :visible.sync="dialogFormVisible"
      @close="clear">
      <el-form v-model="form" style="text-align: left" ref="dataForm">
        <el-form-item label="预约邮箱" :label-width="formLabelWidth" prop="mailAccount">
          <el-input v-model="form.mailAccount" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="书名" :label-width="formLabelWidth" prop="title">
          <el-input v-model="form.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="id" style="height: 0">
          <el-input type="hidden" v-model="form.id" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'BookBorrow',
    data () {
      return {
        dialogFormVisible: false,
        form: {
          title: '',
          mailAccount: ''
        },
        formLabelWidth: '120px'
      }
    },
    methods: {
      clear () {
        this.form = {
          title: '',
          mailAccount: ''
        }
      },
      onSubmit () {
        this.$axios.post('admin/saveBorrow', {
            type: '1',
            bookname: this.form.title,
            bookid: this.form.id,
            mailAccount: this.form.mailAccount
        }
          ).then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'info',
              message: '预约成功'
            })
            this.dialogFormVisible = false
            this.$emit('onSubmit')
          }
        })
      }
    }
  }
</script>
