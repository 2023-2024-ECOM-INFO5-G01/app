
import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getAlertesByPatientAndUser } from 'app/entities/alerte/alerte.reducer';
import { useParams } from 'react-router-dom';
export const AlertePatient = () => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();

  const [alertes, setAlertes] = useState([]);
useEffect(() => {
    if (account && account.login) {
        dispatch(getAlertesByPatientAndUser({ id: id, login: account.login }))
            .then(response => {
                setAlertes((response.payload as any).data);
                console.log(alertes);
            })
            .catch(error => {
                console.error('Une erreur s\'est produite :', error);
            });
    }
}, [account.login, dispatch]);

  return (
    <div>
      <h1>Alerte Page</h1>
      {alertes.map(alerte => (
        <div key={alerte.id}>
          <p>Date: {alerte.date}</p>
          <p>Action: {alerte.action}</p>
          <p>Verif: {alerte.verif ? 'True' : 'False'}</p>
        </div>
      ))}
    </div>
  );
};

export default AlertePatient;