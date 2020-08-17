export default {
    URL: 'http://i3b104.p.ssafy.io:80/noteAPI',
    // URL: 'http://localhost:8080/noteAPI',
    ROUTES: {
        signup: '/nonmember/signUp',
        login: '/login',
        email: '/nonmember/email',
        authSend: '/nonmember/email/authSend',
        authCheck: '/nonmember/email/authCheck',

        logout: '/account/logout',
        modify: '/account/v1/modify',
        delete: '/account/v1/delete',

        onServerInit: '/account/onServerInit',
        createWorkspace: '/band/v1/addBand',
        deleteWorkspace: '/band/v1/deleteBand',
        renameWorkspace: '/band/v1/renameBand',
        // deleteWorkspace: '/account/deleteWorkspace'

        newATBA: '/token/v2/newATBA',
        newATBR: '/token/v2/newATBR',
        newRTBR: '/token/v2/newRTBR',

        getBandMember: '/accountBand/v1/getBandMember',
        inviteBandMember: '/accountBand/v1/inviteBandMember',
        findAccountList: '/accountBand/findAccountList',
        deleteMember: '/accountBand/v1/deleteMember',

        acceptInvite: '/accountBand/v1/acceptInvite',
        declineInvite: '/accountBand/v1/declineInvite',

        noteList: '/note/getNoteSubject',
        createNote: '/note/insertNoteDetail',
        renameNote: '/note/updateNoteDetailSubject',
        deleteNote: '/note/deleteNoteDetail',
        getNote: '/note/getNoteContent',
        saveNote: '/note/updateNoteDetail',
    }
}