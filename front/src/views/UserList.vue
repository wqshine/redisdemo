<template>
  <div>
    <!-- 搜索与添加区域 -->
    <el-row :gutter="0" style="text-align: left;margin-bottom: 15px;">
      <el-col :span="24">
        <template><span style="margin-right: 20px;"><i class="el-icon-s-grid"></i>&nbsp;用户列表</span></template>
        <el-button size="small" type="primary" @click="handleEdit()"><i class="el-icon-plus"></i> 新增用户</el-button>
        <el-button size="small" type="danger" @click="handleDelete()"><i class="el-icon-remove"></i> 批量删除</el-button>
      </el-col>
    </el-row>
    <PageWrap>
      <template v-slot:default="slotProps">
        <div ref="pageContent">
          <!--列表区域-->
          <el-table
            id="roleTable"
            v-loading="loading"
            stripe
            :data="tableData"
            style="width: 100%"
            :default-sort = "{prop: 'id', order: 'ascending'}"
            ref="multipleTable"
            tooltip-effect="dark"
            @selection-change="handleSelectionChange"
            :height="tableData.length>0?slotProps.contentHeight:null"
          >
            <el-table-column
              type="index"
              width="50"
              align="center">
            </el-table-column>
            <el-table-column
              type="selection"
              align="center"
              width="45">
            </el-table-column>
            <el-table-column
              prop="userName"
              label="登录名"
              sortable
              width="100">
            </el-table-column>
            <el-table-column
              prop="fullName"
              width="100"
              label="姓名">
            </el-table-column>
            <el-table-column
              prop="age"
              width="100"
              label="年龄">
            </el-table-column>
            <el-table-column
              width="120"
              align="center"
              label="超级管理员标志">
              <template slot-scope="scope">
                <el-switch v-model="scope.row.admin" disabled></el-switch>
              </template>
            </el-table-column>
            <el-table-column
              prop="roles"
              :formatter="roleData"
              width="250"
              label="关联角色">
            </el-table-column>
            <el-table-column
              width="120"
              align="center"
              label="删除标志">
              <template slot-scope="scope">
                <el-switch v-model="scope.row.deleted" disabled></el-switch>
              </template>
            </el-table-column>
            <el-table-column
              prop="memo"
              label="备注">
            </el-table-column>
          </el-table>
        </div>
      </template>
    </PageWrap>

    <!--表单编辑区域-->
    <el-dialog :title="editTitle" :visible.sync="dialogFormVisible" width="750px" :close-on-click-modal="false">
      <el-form :model="userForm" ref="userForm" :rules="rules">
        <el-form-item label="登录名" :label-width="formLabelWidth" prop="userName">
          <el-input size="small" v-model="userForm.userName" autocomplete="off" placeholder="请输入登录名"></el-input>
        </el-form-item>
        <el-form-item label="姓名" :label-width="formLabelWidth" prop="fullName">
          <el-input size="small" v-model="userForm.fullName" autocomplete="off" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="年龄" :label-width="formLabelWidth" prop="age">
          <el-input size="small" v-model="userForm.age" autocomplete="off" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="超级管理员" :label-width="formLabelWidth" prop="admin">
          <el-switch v-model="userForm.admin"></el-switch>
        </el-form-item>
        <el-form-item label="关联角色" :label-width="formLabelWidth" prop="roles">
          <el-transfer
            v-model="associateRole"
            filterable
            :titles="['未关联角色', '已关联角色']"
            :props="{ key: 'id',label: 'name' }"
            @change="roleChange"
            :data="roleList">
          </el-transfer>
        </el-form-item>
        <el-form-item label="删除标志" :label-width="formLabelWidth" prop="deleted">
          <el-switch v-model="userForm.deleted"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :disabled="isSaving" size="small" type="primary" @click="saveUser('userForm')">确 定</el-button>
        <el-button size="small" @click="resetForm()">重 置</el-button>
        <el-button size="small" @click="close()">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import PageWrap from '../components/PageWrap';
import {listUsers,findUser,deleteUsers,saveUser} from '../api/user';
import {listRoles} from '../api/role';

export default {
  components: {
    PageWrap,
  },
  name: 'UserList',
  data() {
    return {
      loading: true,
      tableData: [],
      dialogFormVisible:false,
      formLabelWidth: '90px',
      editTitle:'',
      roleList:[],
      associateRole:[],
      rules: {
        userName: [
          { required: true, message: '请输入登录名', trigger: 'blur' }
        ],
        fullName: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ]
      },
      checkList:[],
      deleteList:[],
      isSaving:false,
      userId:'',
      userForm: {},
      multipleSelection: []
    }
  },
  methods: {
    /*获取用户列表*/
    listUsers(){
      listUsers().then( response => {
        if(response&&response.success){
          this.tableData=response.data;
        }
        this.loading = false;
      })
    },
    /*获取角色列表*/
    getRolesList(){
      listRoles().then( response => {
        if(response.success){
          this.roleList = response.data;
        }
      })
    },
    /*列表中的关联角色格式化*/
    roleData(row){
      let roleArr = [];
      row.roles.forEach((item,index)=>{
        roleArr.push(item.name+'；')
      });
      return roleArr
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    /*编辑用户*/
    handleEdit() {
      this.dialogFormVisible = true;
      this.editTitle = '新增用户';
      this.userForm = {};
    },
    roleChange(value, direction, movedKeys){
      let list = [];
      value.map((x) => {
        this.roleList.map((k) => {
          if (k.id == x) {
            return list.push(k)
          }
        })
      });
      this.userForm.roles = list;
    },
    /*关联角色列表初始化*/
    initAssociateRole(roles){
      let list = [];
      if(roles&&roles.length>0){
        roles.map((x) => {
          return list.push(x.id)
        });
      }
      this.associateRole = list;
    },
    /*保存角色*/
    saveUser(form){
      this.$refs[form].validate((valid) => {
        if (valid) {
          this.isSaving = true;
          saveUser(this.userForm).then(response =>{
            if(response.success){
              this.dialogFormVisible = false;
              this.alert("success",response.msg);
              this.listUsers();
            }else {
              this.alert("error",response.msg);
            }
            this.isSaving = false;
          })
        } else {
          return false;
        }
      });
    },
    /*删除角色*/
    handleDelete() {
      if(this.multipleSelection===null||this.multipleSelection.length===0){
        this.alert("error","请选择至少一个用户");
      }else {
        this.deleteList = [];
        this.$confirm('此操作将删除所选用户, 是否继续?', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(() => {
          this.deleteList = this.multipleSelection;
          deleteUsers(this.deleteList).then(response =>{
            if(response.code===200){
              this.alert("success","删除成功");
              this.deleteList = [];
              this.listUsers();
            }else {
              this.alert("error",response.msg);
            }
          })
        }).catch(()=>{})
      }
    },
    /*取消按钮*/
    close(){
      /*清空表单*/
      // this.form = [];
      this.resetForm('userForm');
      this.dialogFormVisible = false;
    },
    /*重置表单*/
    resetForm(){
      if(this.editTitle === '编辑用户')
        this.handleEdit(this.userForm);
      else
        this.handleEdit();
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
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    }
  },
  mounted() {
    this.listUsers();
    this.getRolesList();
    this.$nextTick(() =>{

    });
  }
}
</script>

<style scoped>

</style>
