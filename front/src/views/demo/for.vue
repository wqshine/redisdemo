<template>
  <div id="for">
    <p>{{html}}</p>
    <p v-html="html"></p>
    <p v-if="visible">hello</p>

    <ul>
      <li v-for="n in filter" v-bind:key="index">{{n}} </li>
    </ul>

    <el-row >
      <el-col :span="8" style="text-align: center">
        <div class="grid-content bg-purple">
          <p style="background: #42b983">left</p>
        </div>
      </el-col>
      <el-col :span="8" style="text-align: center">
        <div>
          <p>center</p>
        </div>
      </el-col>
      <el-col :span="8" style="text-align: center">
        <div>
          <p>right</p>
        </div>
      </el-col>
    </el-row>

    <Test>
      <p slot="slot1" v-html="html"></p>
    </Test>

  </div>
</template>


<script>
import Test from "@/views/demo/Test";
export default {
  name: 'for',
  components: {Test},
  data () {
    return {
      html: '<span style="color: #42b983">html</span>',
      visible: true,
      number: [1, 2, 3, 4, 5, 6, 7, 8, 9]
    }
  },
  computed: {
    filter () {
      return this.number.filter(number => number % 2 === 0)
    }
  }
}
</script>

<style scoped>
.el-row{
  margin-bottom: 20px;
}
.el-cow{
  border-radius: 4px;
}
.bg-purple {
  background: #d3dce6;
}
</style>
