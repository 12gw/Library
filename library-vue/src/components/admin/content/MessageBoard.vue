<template>
  <div style="text-align: left">
    <el-dialog
      title="修改留言"
      :visible.sync="dialogFormVisible"
      @close="clear">
      <el-form v-model="form" style="text-align: left" ref="dataForm">
        <el-form-item label="内容" :label-width="formLabelWidth" prop="message">
          <el-input v-model="form.message" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态" :label-width="formLabelWidth" prop="status">
          <el-select v-model="form.status" placeholder="请选择分类">
            <el-option label="正常" value="0"></el-option>
            <el-option label="删除" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分类" :label-width="formLabelWidth" prop="type">
          <el-select v-model="form.type" placeholder="请选择分类">
            <el-option label="建议" value="1"></el-option>
            <el-option label="读后感" value="2"></el-option>
          </el-select>
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
    name: 'MessageBoard',
    data () {
      return {
        dialogFormVisible: false,
        form: {
          message: '',
          type: '',
          status: ''
        },
        formLabelWidth: '120px'
      }
    },
    methods: {
      clear () {
        this.form = {
          message: '',
          type: '',
          status: ''
        }
      },
      onSubmit () {
        let param = new URLSearchParams()
        param.append('id', this.form.id)
        param.append('message', this.form.message)
        param.append('type', this.form.type)
        param.append('status', this.form.status)
        this.$axios
          .put('admin/updateMessage', param
          ).then(resp => {
          if (resp && resp.data.code === 200) {
            this.dialogFormVisible = false
            this.$emit('onSubmit')
          }
        })
      }
    }
  }
</script>
