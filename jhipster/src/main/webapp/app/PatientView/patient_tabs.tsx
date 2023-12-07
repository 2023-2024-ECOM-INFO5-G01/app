import React from 'react'

export const PatientTabs = (props) => {
  const changeTab = props.changeTab;

  return (
    <div className="menu_patient">
      <nav>
        <ul>
          <li>
            <button className="bouton_menu_patient" onClick={() => changeTab('graphe')}>Courbes</button>
          </li>
          <li>
            <button className="bouton_menu_patient" onClick={() => changeTab('note')}>Notes</button>
          </li>
          <li>
            <button className="bouton_menu_patient" onClick={() => changeTab('rappel')}>Tâches</button>
          </li>
          <li>
            <button className="bouton_menu_patient" onClick={() => changeTab('alerte')}>Alertes</button>
          </li>
        </ul>
      </nav>
    </div>
  )
}
