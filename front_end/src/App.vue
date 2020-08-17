<template>
  <v-app id="app">

    <NavBar />
    <SideBar v-if="!$store.state.isShareMode" />
    <SideBarShare v-if="$store.state.isShareMode" />
    <Home />
  </v-app>
</template>

<script>
import NavBar from "./components/NavBar.vue"
import SideBar from "./components/SideBar.vue"
import SideBarShare from "./components/SideBarShare.vue"
import Home from "./views/Home.vue"

export default {
  name: "App",

  components: {
    NavBar,
    SideBar,
    SideBarShare,
    Home,
  },

  mounted() {
    if (localStorage.getItem('authorization')) {
      this.$store.dispatch('initUserInfo')
      this.$store.commit('SET_IS_SHARE', true)
      this.$store.commit('SET_IS_DRAWER_SHARE', true)
    } else {
      this.$store.commit('SET_IS_SHARE', false)
      this.$store.commit('SET_IS_DRAWER', true)
    }
  },

  // updated(){
  //   var div = document.getElementById("compiledMarkdown");
  //   if(this.$vuetify.theme.dark == true)
  //     div.style.color = "white";
  //   else
  //     div.style.color = "black";
  // },
};
</script>

<style>
  html, body {
    margin: 0;
    height: 100%;
    font-family: 'Helvetica Neue', Arial, sans-serif;
    color: #333;
  }

  .v-application code {
    all: unset;
    
    background: #474949!important;
    color: #d1d9e1!important;
  }

  pre code {
    display: block!important;
  overflow-x: auto!important;
  padding: 0.5em!important;
  background: #474949;
  color: #d1d9e1;
  }

</style>
