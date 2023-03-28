<template>
  <div>
    <el-form ref="form" :model="userInfo" label-width="80px">
      <el-form-item label="登录用户">
        <el-input v-model="userInfo.userName" :readonly="readOnly"></el-input>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="userInfo.fullName" :readonly="readOnly"></el-input>
      </el-form-item>
      <el-form-item label="年龄">
        <el-input v-model="userInfo.age" :readonly="readOnly"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="userInfo.sexEnum" :disabled="disabled">
          <el-radio label="Male">男</el-radio>
          <el-radio label="Female">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="userInfo.phoneNumber" :readonly="readOnly"></el-input>
      </el-form-item>
      <el-form-item label="qq">
        <el-input v-model="userInfo.qq" :readonly="readOnly"></el-input>
      </el-form-item>
    </el-form>
    <div align="center">
      <el-button-group >
        <el-button type="primary" size="small" @click="handleEdit()">编辑用户信息</el-button>
        <el-button size="small" type="primary" @click="save()" :disabled="disabled">保 存</el-button>
        <el-button size="small" @click="close()" :disabled="disabled">取 消</el-button>
      </el-button-group>
    </div>
  </div>
</template>

<script>
import {findUser} from '../api/user';
import {saveUser} from '../api/user';

export default {
  name: 'UserInfo',
  data(){
    return {
      readOnly:true,
      disabled:true,
      dialogFormVisible:false,
      userInfo:{
        userName:'',
        fullName:'',
        age:'',
        sexEnum:'',
        phoneNumber:'',
        qq:''
      }
    }
  },
  methods:{
    findUser(){
      var currentUser=JSON.parse(localStorage.getItem('currentUser'));
      findUser(currentUser.id).then(response=>{
        if(response&&response.success===true){
          this.userInfo=response.data;
        }
      })
    },
    handleEdit(){
      this.readOnly=false;
      this.disabled=false;
    },
    save(){
      saveUser(this.userInfo).then(response=>{
        if(response&&response.success===true){
          this.alert("success","保存成功");
        }else
          this.alert("error",response.message);
      })
      this.readOnly=true;
      this.disabled=true;
    },
    /*提示*/
    alert(type,msg) {
      this.$alert(msg, '', {
        confirmButtonText: '确定',
        center: true,
        customClass:"alert",
        type:type
      });
    },
    close(){
      this.readOnly=true;
      this.disabled=true;
    }

  },
  mounted () {
    this.findUser();
  }
}
</script>

<style scoped>

</style>
