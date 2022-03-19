package com.learntodroid.androidminesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnCellClickListener {
    public static final long TIMER_LENGTH = 999000L;    // 999 seconds in milliseconds
    public static final int BOMBAS = 5;
    public static final int TAMANIO_CASILLA = 10;

    private ReciclaTablero reciclaTablero;
    private RecyclerView CUADRICULA;
    private TextView reinicio, tiempo, bandera, banderin;
    private JuegoBuscaMinas buscaMinas;
    private CountDownTimer cuentaRegresiva;
    private int tiempoPasado;
    private boolean timerStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CUADRICULA = findViewById(R.id.activity_main_grid);
        CUADRICULA.setLayoutManager(new GridLayoutManager(this, 10));

        tiempo = findViewById(R.id.activity_main_timer);
        timerStarted = false;
        cuentaRegresiva = new CountDownTimer(TIMER_LENGTH, 1000) {
            public void onTick(long millisUntilFinished) {
                tiempoPasado += 1;
                tiempo.setText(String.format("%03d", tiempoPasado));
            }

            public void onFinish() {
                buscaMinas.outOfTime();
                Toast.makeText(getApplicationContext(), "PERDISTE: EL TIEMPO SE ACABO", Toast.LENGTH_SHORT).show();
                buscaMinas.getMineGrid().revealAllBombs();
                reciclaTablero.setCells(buscaMinas.getMineGrid().getCells());
            }
        };

        banderin = findViewById(R.id.activity_main_flagsleft);

        buscaMinas = new JuegoBuscaMinas(TAMANIO_CASILLA, BOMBAS);
        banderin.setText(String.format("%03d", buscaMinas.getConteoBomba() - buscaMinas.getConteoBandera()));
        reciclaTablero = new ReciclaTablero(buscaMinas.getMineGrid().getCells(), this);
        CUADRICULA.setAdapter(reciclaTablero);

        reinicio = findViewById(R.id.activity_main_smiley);
        reinicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscaMinas = new JuegoBuscaMinas(TAMANIO_CASILLA, BOMBAS);
                reciclaTablero.setCells(buscaMinas.getMineGrid().getCells());
                timerStarted = false;
                cuentaRegresiva.cancel();
                tiempoPasado = 0;
                tiempo.setText(R.string.default_count);
                banderin.setText(String.format("%03f", buscaMinas.getConteoBomba() - buscaMinas.getConteoBandera()));
            }
        });

        bandera = findViewById(R.id.activity_main_flag);
        bandera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscaMinas.toggleMode();
                if (buscaMinas.isBandera()) {
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(0xFFFFFFFF);
                    border.setStroke(1, 0xFF000000);
                    bandera.setBackground(border);
                } else {
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(0xFFFFFFFF);
                    bandera.setBackground(border);
                }
            }
        });
    }

    @Override
    public void cellClick(Celda celda) {
        buscaMinas.handleCellClick(celda);

        banderin.setText(String.format("%03d", buscaMinas.getConteoBomba() - buscaMinas.getConteoBandera()));

        if (!timerStarted) {
            cuentaRegresiva.start();
            timerStarted = true;
        }

        if (buscaMinas.isJuegoTerminado()) {
            cuentaRegresiva.cancel();
            Toast.makeText(getApplicationContext(), "BOOOOOM!!! PERDISTE!!!", Toast.LENGTH_SHORT).show();
            buscaMinas.getMineGrid().revealAllBombs();
        }

        if (buscaMinas.isGameWon()) {
            cuentaRegresiva.cancel();
            Toast.makeText(getApplicationContext(), "HAZ GANADO", Toast.LENGTH_SHORT).show();
            buscaMinas.getMineGrid().revealAllBombs();
        }

        reciclaTablero.setCells(buscaMinas.getMineGrid().getCells());
    }
}