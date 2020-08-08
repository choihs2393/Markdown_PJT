<template> 
    <v-main>
        <SideBarShare />
        <div id="div_test">
            <span>이 곳은 그룹이 공유하는 마크다운 문서들을 바둑판 형식으로 보여줄 공간입니다.</span>
        </div>
    </v-main>
</template>

<script>
import SideBarShare from "@/components/SideBarShare.vue";

export default {
    components: {
        SideBarShare
    }
}
</script>

<style>
#div_test {
    width: 100%;
    height: 30;
    line-height: 30;
    text-align: center;
}
</style>