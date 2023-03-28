<template>
  <el-row class="tac">
    <el-col :span="24">
      <el-menu
        :default-active="activeIndex"
        router
        @open="handleOpen"
        @close="handleClose">
        <div v-for="(item,index) in routerData">
          <el-submenu v-if="item.children" :index="item.path">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>{{item.meta.title}}</span>
            </template>
            <div v-for="(node,indexs) in item.children">
              <el-menu-item v-if="!node.children" :index="node.path"><i class="el-icon-document"></i>{{node.meta.title}}</el-menu-item>
              <el-submenu v-if="node.children" :index="node.path">
                <template slot="title">
                  <i class="el-icon-document"></i>
                  <span>{{node.meta.title}}</span></template>
                <div v-for="(point,pointIndex) in node.children">
                  <el-menu-item :index="point.path">{{point.meta.title}}</el-menu-item>
                </div>

              </el-submenu>
            </div>

          </el-submenu>
          <el-menu-item v-if="!item.children" :index="item.path">
            <i class="el-icon-menu"></i>
            <span slot="title">{{item.meta.title}}</span>
          </el-menu-item>
        </div>

      </el-menu>
    </el-col>
  </el-row>

</template>

<script>
export default {
  name: "Aside",
  data(){
    return{
      activeIndex:'',
      routerData:[]
    }
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    getRouter(){
      this.routerData=this.$router.options.routes;

      // for (let i = 0; i < this.$router.options.routes.length; i++) {
      //   if(this.$router.options.routes[i].path == 'menu'){
      //     this.routerData=this.$router.options.routes[i].children;
      //   }
      // }

      console.log(this.routerData);
    },
    onload(){
      // if(window.location.hash.split('/')==='about')
      //
      // else
      return window.location.hash.split('/')[1];
    }
  },
  mounted: function () {
    this.activeIndex = this.onload();
    this.getRouter();
  }
}
</script>

<style scoped>
.el-menu-item {
  height: 40px;
  line-height: 40px;
}
</style>
