package common.db.dao

import common.db.MembershipEvent
import common.db.VisitEvent
import common.db.dao.module.ModuleDAO

interface DAO {

    val visitDAO: ModuleDAO<VisitEvent>
    val membershipDAO: ModuleDAO<MembershipEvent>

}
