import Vue from 'vue'
import App from './App.vue'
import router from './router'
import {
  Button, Container, Header, Aside, Row, Col, Menu, Submenu, MenuItem, Footer,
  Main, Table, TableColumn, Pagination, Dialog, Form, FormItem, Input, Checkbox,
  Popover, Link, Loading, Message, MessageBox, Switch, Transfer, Tree, Dropdown, DropdownMenu, DropdownItem,
  Card, Select, Option, DatePicker, Radio, RadioGroup, ButtonGroup
} from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';

Vue.component(Button.name,Button);
Vue.component(Container.name,Container);
Vue.component(Header.name,Header);
Vue.component(Aside.name,Aside);
Vue.component(Row.name,Row);
Vue.component(Col.name,Col);
Vue.component(Menu.name,Menu);
Vue.component(Submenu.name,Submenu);
Vue.component(MenuItem.name,MenuItem);
Vue.component(Footer.name,Footer);
Vue.component(Main.name,Main);
Vue.component(Table.name,Table);
Vue.component(TableColumn.name,TableColumn);
Vue.component(Pagination.name,Pagination);
Vue.component(Dialog.name,Dialog);
Vue.component(Form.name,Form);
Vue.component(FormItem.name,FormItem);
Vue.component(Input.name,Input);
Vue.component(Checkbox.name,Checkbox);
Vue.component(Popover.name,Popover);
Vue.component(Link.name,Link);
Vue.component(Switch.name,Switch);
Vue.component(Transfer.name,Transfer);
Vue.component(Tree.name,Tree);
Vue.component(Dropdown.name,Dropdown);
Vue.component(DropdownMenu.name,DropdownMenu);
Vue.component(DropdownItem.name,DropdownItem);
Vue.component(Card.name,Card);
Vue.component(Select.name,Select);
Vue.component(Option.name,Option);
Vue.component(DatePicker.name,DatePicker);
Vue.component(Radio.name,Radio);
Vue.component(RadioGroup.name,RadioGroup);
Vue.component(ButtonGroup.name,ButtonGroup);
Vue.use(Loading.directive);


Vue.config.productionTip = false;
Vue.prototype.$message = Message;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$prompt = MessageBox.prompt;


new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
