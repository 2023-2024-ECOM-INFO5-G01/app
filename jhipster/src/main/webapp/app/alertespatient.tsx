
import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getAlertesByPatientAndUser ,toggleVerif} from 'app/entities/alerte/alerte.reducer';
import { useParams } from 'react-router-dom';
import './shared/layout/menus/Alerte.css'; // Import your CSS file

export const AlertePatient = ({ idprops }: { idprops: string }) => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();

  const [alertes, setAlertes] = useState([]);
useEffect(() => {
    if (account && account.login) {
        dispatch(getAlertesByPatientAndUser({ id: idprops, login: account.login }))
            .then(response => {
                setAlertes((response.payload as any).data);
                console.log(alertes);
            })
            .catch(error => {
                console.error('Une erreur s\'est produite :', error);
            });
    }
}, [account.login, dispatch]);

const handleToggleVerif = (alertId: string | number) => { // New function to handle click
    dispatch(toggleVerif(alertId))
    .then(() => {
      // Refresh the alertes after toggling verif
      if (account && account.login) {
        dispatch(getAlertesByPatientAndUser({ id: idprops, login: account.login })) // Use patient's id here
        .then(response => {
          setAlertes((response.payload as any).data);
        });
      }
    });
  };
return (
    <div>
      {alertes.map(alerte => (
        <div key={alerte.id} className="alerte">
          <div className="alerte-content">
            <div className="alerte-icon">⚠️</div>
            <div>
              <h2>Alerte dénutrition</h2>
              <p>Action: {alerte.action}</p>
              <p>Date: {alerte.date}</p>
            </div>
            <button className="alerte-check" onClick={() => handleToggleVerif(alerte.id)}>
            {alerte.verif ? '✅' : '⬜'}
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default AlertePatient;