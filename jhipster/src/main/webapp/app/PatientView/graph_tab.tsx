import { Col, Row } from 'reactstrap';
import { Line } from 'react-chartjs-2';
import React, { useEffect, useState } from 'react';
import { ChartData, ChartOptions, DefaultDataPoint } from 'chart.js';
import { useAppDispatch } from 'app/config/store';
import { useParams } from 'react-router-dom';
import { getIMC } from 'app/entities/imc/imc.reducer';
import { PayloadAction } from '@reduxjs/toolkit';
import { AxiosResponse } from 'axios';
import { IIMC } from 'app/shared/model/imc.model';
import { getPoids } from 'app/entities/poids/poids.reducer';
import { IPoids } from 'app/shared/model/poids.model';
import { getEpas } from 'app/entities/epa/epa.reducer';
import { IEPA } from 'app/shared/model/epa.model';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const DAY = 86400000;

export const GraphTab = (props) => {

  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  const [poidsDates, setPoidsDates] = useState<string[]>([]); // Tableau de dates (chaînes)
  const [poidsValues, setPoidsValues] = useState([]); // Tableau de valeurs d'IMC (numériques)

  const [epaDates, setEpaDates] = useState<string[]>([]); // Tableau de dates (chaînes)
  const [epaValues, setEpaValues] = useState([]); // Tableau de valeurs d'IMC (numériques)

  const [timePeriod, setTimePeriod] = useState(14);

  useEffect(() => {
    dispatch(getPoids(id)).then((response: PayloadAction<any>) => {
      if (response.payload && response.payload.data) {
        const poidsData = response.payload.data as IPoids[];
        const dates = poidsData.map((poids: IPoids) => poids.date);
        const values = poidsData.map((poids: IPoids) => ({ y: poids.poids, x: new Date(poids.date)}));
        setPoidsDates(dates);
        setPoidsValues(values);
      }
    });
    dispatch(getEpas(id)).then((response: PayloadAction<AxiosResponse, any, any>) => {
        if (response.payload && response.payload.data) {
          const epaData = response.payload.data as IEPA[];
          const dates = epaData.map((epa: IEPA) => epa.date);
          const values = epaData.map((epa: IEPA) => ({ y: epa.epa, x: new Date(epa.date) }));
          setEpaDates(dates);
          setEpaValues(values);
        }
      }
    );
  }, []);

  const currentTime = new Date().getTime();

  const createData = (name: string, labels: any[], data: number[]) => {
    return {
      labels,
      datasets: [
        {
          label: name + ' du patient',
          data,
          fill: false,
          borderColor: 'rgb(75, 192, 192)'
        }
      ] as DefaultDataPoint<any>
    };
  };

  const createOption = (type: string): any => {
    return {
      responsive: true,
      plugins: {
        legend: {
          position: 'top' as const
        },
        title: {
          display: true,
          text: 'Courbe ' + type + ' du patient'
        },
        tooltips: {
          enabled: false,  // Désactiver les tooltips
        },
      },
      scales: {
        x: {
          max: currentTime,
          min: new Date(currentTime - (timePeriod * DAY)),
          type: 'linear',
          ticks: {
            callback(value, index, ticks) {
              const monthNames = [
                'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'
              ];
              const date = new Date(value);
              return date.getDate() + ' ' + monthNames[date.getMonth()] + ' ' + date.getFullYear();
            }
          }
        }
      }
    };
  };

  const handleTimePeriod = (value) => {
    if (value === -1) {
      const days = currentTime - Math.min(new Date(poidsDates[0]).getTime(), new Date(epaDates[0]).getTime());
      setTimePeriod(days / DAY);
    } else {
      setTimePeriod(value);
    }
  };

  return <div>
    <div>
      <Row>
        <Col md="6">
          <Line options={createOption('poids')} data={createData('Poids', poidsDates, poidsValues)} />
        </Col>
        <Col md="6">
          <Line options={createOption('epa')} data={createData('EPA', epaDates, epaValues)} />
        </Col>
      </Row>
    </div>
    <div className='buttons_graph'>
      <button className='bouton_modif_courbe' onClick={() => handleTimePeriod(14)}>2 semaines</button>
      <button className='bouton_modif_courbe' onClick={() => handleTimePeriod(30)}>1 mois</button>
      <button className='bouton_modif_courbe' onClick={() => handleTimePeriod(365)}>1 an</button>
      <button className='bouton_modif_courbe' onClick={() => handleTimePeriod(-1)}>Tout</button>
    </div>
  </div>;
};
