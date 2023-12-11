import React from 'react'
import { useAppSelector } from 'app/config/store';
export const PatientTabs = (props) => {
  const changeTab = props.changeTab;
  const account = useAppSelector(state => state.authentication.account);
  const userHasRequiredRole = account.authorities.some(role => ['ROLE_MEDECIN', 'ROLE_ADMIN'].includes(role));

  return (
    <div className="menu_patient">
      <nav>
        <ul>
          {userHasRequiredRole && (
            <>
              <li>
                <button onClick={() => changeTab('graphe')}>Courbes</button>
              </li>
              <li>
                <button onClick={() => changeTab('note')}>Notes</button>
              </li>
              </>
          )}
          <li>
            <button onClick={() => changeTab('rappel')}>Tâches</button>
          </li>
          {userHasRequiredRole && (
              <li>
                <button onClick={() => changeTab('alerte')}>Alertes</button>
              </li>
          )}
          
        </ul>
      </nav>
    </div>
  )
}