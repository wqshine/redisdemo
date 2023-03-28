<template>
  <div>
    <el-row>
      <el-col :span="4">
        <p class="p">{{ message }}</p>
      </el-col>

      <el-col :span="20" style="text-align: right;">
        <el-popover
          v-if="currentUser"
          placement="bottom"
          trigger="click">
          <el-link :underline="false" slot="reference">你好，{{currentUser.fullName}}</el-link>
          <div>
            <ul class="popover_list">
              <li @click="dialogPasswordVisible = true"><i class="el-icon-edit"></i> 修改密码</li>
              <li @click="logout()"><i class="el-icon-turn-off"></i> 退出</li>
            </ul>
          </div>
        </el-popover>
      </el-col>
    </el-row>

    <el-dialog :visible.sync="dialogFormVisible" width="450px" :close-on-click-modal="false" :show-close="false" style="text-align: left;">
      <el-form ref="form" :model="form">
        <el-form-item>
          <div style="font-size: 2em;">LOG IN</div>
        </el-form-item>
        <el-form-item prop="username" :rules="[ { required: true, message: '请输入账号'} ]">
          <el-input v-model="form.username" prefix-icon="el-icon-user-solid" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item prop="password" :rules="[ { required: true, message: '请输入密码'} ]">
          <el-input v-model="form.password" prefix-icon="el-icon-lock" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="form.rememberMe">记住密码</el-checkbox>
        </el-form-item>
        <el-form-item style="margin-bottom: 5px;">
          <el-button type="primary" @click="onSubmit()" style="width: 100%;">登 录</el-button>
        </el-form-item>
      </el-form>
      <div v-if="errorMsg" style="color: #F56C6C;width: 100%;text-align: center;line-height: 24px;">{{errorMsg}}</div>
    </el-dialog>

    <el-dialog
      :visible.sync="dialogPasswordVisible"
      width="400px"
      :close-on-click-modal="false"
      @open="resetPasswordForm('passwordForm')"
      class="my-info-dialog">
      <el-form :model="passwordForm" ref="passwordForm" :rules="passwordRules" label-position="left">
        <el-form-item label="原密码" prop="oldPassword" :label-width="formLabelWidth">
          <el-input v-model="passwordForm.oldPassword" autocomplete="off" placeholder="请输入原密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword" :label-width="formLabelWidth">
          <el-input v-model="passwordForm.newPassword" autocomplete="off" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="repeatPassword" :label-width="formLabelWidth">
          <el-input v-model="passwordForm.repeatPassword" autocomplete="off" placeholder="请确认新密码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetPasswordForm('passwordForm')">重 置</el-button>
        <el-button type="primary" @click="changePassword('passwordForm')">确认修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {login,logout,changePassword} from '../api/login'
import {getCurrentUser} from '../api/user'

export default {
  name: "Login",
  data(){
    var repeatPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return{
      message:"学生管理系统",
      currentUser:'',
      dialogFormVisible:false,
      errorMsg:'',
      form: {
        username: '',
        password: '',
        rememberMe:'',
      },
      dialogPasswordVisible: false,
      formLabelWidth: '80px',
      passwordForm:{
        oldPassword:'',
        newPassword:'',
        repeatPassword:''
      },
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入原密码', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        repeatPassword: [
          { required: true,validator: repeatPassword, trigger: 'blur' }
        ]
      }

    }
  },
  methods:{
    /**登录**/
    initUser(){
      getCurrentUser().then(response => {
        if(!response){
          this.dialogFormVisible = true;
        }else {
          this.currentUser = response;
          /*将当前用户信息保存至全局，方便后续取用*/
          localStorage.setItem('currentUser',JSON.stringify(this.currentUser));
        }
      })
    },
    onSubmit(){
      login(this.form).then(response => {
        if(response){
          this.dialogFormVisible =false;
          this.$router.go(0);
        }else {
          this.errorMsg = response.message;
        }
      })
    },
    /*登出*/
    logout(){
      logout().then(response => {
        console.log(response);
        this.$router.go(0);
      })
    },
    /*修改密码*/
    changePassword(form){
      this.$refs[form].validate((valid) => {
        if (valid) {
          changePassword(this.passwordForm).then(response =>{
            if(response.code===200){
              this.dialogPasswordVisible = false;
              this.alert("success",response.message);
            }else {
              this.alert("error",response.message);
            }
          })
        } else {
          return false;
        }
      });
    },
    /*重置密码表单*/
    resetPasswordForm(formName){
      if(this.$refs[formName])
        this.$refs[formName].resetFields();
    },
    /*提示*/
    alert(type,msg) {
      this.$alert(msg, '', {
        confirmButtonText: '确定',
        center: true,
        customClass:"alert",
        type:type
      })
    }
  },
  mounted () {
    this.initUser();
  }
}
</script>

<style>
.p{
  color: #F56C6C;
  line-height: 20px;
  font-size: 20px
}
.popover_list li{
  padding: 0 15px;
  line-height: 30px;
}
.popover_list li:hover{
  background-color: #eee;
  cursor: pointer;
}
</style>



