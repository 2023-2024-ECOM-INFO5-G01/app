import React from 'react'

export const PatientTabs = (props) => {
  const changeTab = props.changeTab;

  return (
    <div className="menu_patient">
      <nav>
        <ul>
          <li>
            <button onClick={() => changeTab('graphe')}>Courbes</button>
          </li>
          <li>
            <button onClick={() => changeTab('note')}>Notes</button>
          </li>
          <li>
            <button onClick={() => changeTab('rappel')}>Tâches</button>
          </li>
          <li>
            <button onClick={() => changeTab('alerte')}>Alertes</button>
          </li>
        </ul>
      </nav>
    </div>
  )
}
