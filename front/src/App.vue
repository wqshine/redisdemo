<template>
  <el-container id="app">
    <el-header><Header/></el-header>
    <el-container>
      <el-aside width="200px">
        <Aside/>
      </el-aside>
      <el-container>
        <el-main><router-view/></el-main>
        <el-footer>Footer</el-footer>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
  import Header from "./components/Header";
  import Aside from "./components/Aside";

  export default {
    components: {Header,Aside},
    data(){
      return{
        mianHeight:null,
      }
    }
  }

</script>

<style lang="less">
  body,html{
    height: 100%;
    margin: 0;
  }
  .el-header, .el-footer {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 60px;
  }

  .el-aside {
    background-color: #D3DCE6;
    color: #333;
   // text-align: center;
    height: 100%;// 设置主体 main 高度
    /*line-height: 200px;*/
  }

  .el-main {
    background-color: #E9EEF3;
    color: #333;
    height: calc(100vh - 120px);// 设置主体 main 高度
    /*line-height: 160px;*/
  }

  body > .el-container {
    margin-bottom: 40px;
    height: 100%;
  }

  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    /*line-height: 260px;*/
  }

  .el-container:nth-child(7) .el-aside {
    /*line-height: 320px;*/
  }

  /*去掉右边滚动条*/
  /*::-webkit-scrollbar{
    width: 0 !important;
  }*/
</style>
