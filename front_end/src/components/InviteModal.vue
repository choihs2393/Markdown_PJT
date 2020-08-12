<template>
<v-dialog v-model="$store.state.isInviteModal" max-width="400px">
  <v-card 
    class="mx-auto"
  >
    <v-toolbar
      color="rgb(255, 179, 102)"
      dark
    >
      <v-text-field
        autofocus
        v-model.lazy="search"
        append-icon="mdi-plus"
        label="Search Email"
        hide-details
        @click:append = "inviteBandMember"
        @keyup="$store.state.noSuchMemberAlert=false"
      ></v-text-field>
    </v-toolbar>
      <v-alert class="ma-2" dense outlined persistent type="error" icon="mdi-cloud-alert" v-model="$store.state.noSuchMemberAlert">
        가입된 회원이 아닙니다.
      </v-alert>
    <v-list>
      <v-list-item
        v-for="item in $store.state.workspaceMemberList"
        :key="item.no"
      >
        <v-list-item-icon>
          <v-icon v-if="!(item.status)" color="pink">mdi-star</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title v-text="item.name"></v-list-item-title>
        </v-list-item-content>

        <v-list-item-group v-model="isOwner">
        <v-chip small color="orange" rounded filter dark v-if="item.status===2" style="margin-right: 1em">수락 대기 중</v-chip>

        <v-btn small color="orange" rounded outlined v-if="item.status!=0">내보내기</v-btn>
      </v-list-item-group>
      </v-list-item>
    </v-list>
  </v-card>
</v-dialog>
</template>

<script>
export default {
    name: 'InviteModal',
    data () {
      return {
        search: '',
        isOwner: false,
      }
    },
    mounted() {
      if(this.$store.state.isInviteModal){
        if(this.$store.state.workspaceMemberList.find(element => element.no === this.$store.state.userInfo.no).status === 0){
          isOwner = true;
        }
      }
    },
    methods: {
      inviteBandMember() {
        const findAccountList = {
          email: this.search
        }
        this.$store.dispatch("findAccountList", findAccountList)
        this.search = ''
      }
    },
}
</script>