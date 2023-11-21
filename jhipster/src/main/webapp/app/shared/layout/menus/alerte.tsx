
import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getAlertesByUser } from 'app/entities/alerte/alerte.reducer';

export const Alerte = () => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();

  const [alertes, setAlertes] = useState([]);
  useEffect(() => {
    if (account && account.login) {
      dispatch(getAlertesByUser(account.login))
      .then(response => {
        setAlertes((response.payload as any).data);
        console.log(alertes);
      })
      .catch(error => {
        console.error('Une erreur s\'est produite :', error);
      }
      );
      

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

export default Alerte;