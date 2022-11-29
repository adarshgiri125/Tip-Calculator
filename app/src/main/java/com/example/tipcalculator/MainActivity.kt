package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.components.Inputfield
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import com.example.tipcalculator.util.CalculateTotalBill
import com.example.tipcalculator.util.calculateTotalTip
import com.example.tipcalculator.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
              Myapp{
                  Maincontent()
                 
              }

            }
        }
    }


@Composable
fun Myapp(content: @Composable () -> Unit) {
    TipCalculatorTheme {
        // A surface container using the 'background' color from the theme
        Surface(

            color = MaterialTheme.colors.background
        ) {
            content()

        }


    }
}
@Preview
@Composable
fun TopHeader(totalPerPerson : Double = 100.0){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(130.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(13.dp))),
    color = Color.Unspecified) {
        Column(modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
            val Total = "%.2f".format(totalPerPerson)
            Text(text = "Total Per Person", style = MaterialTheme.typography.h5)
            Text(text = "$$Total",
                fontWeight = FontWeight.Bold,style = MaterialTheme.typography.h4)

        }

    }
}



@Preview
@Composable
fun Maincontent(){
    val tipamountstate = remember {
        mutableStateOf(0.0)
    }


    val SplitByState = remember {
        mutableStateOf(1)
    }
    val Range = IntRange(start = 1, endInclusive = 100)

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    Column(modifier = Modifier.padding(all = 12.dp)) {
        Mybill(SplitByState = SplitByState,
            Range = Range,
        tipamountstate = tipamountstate,
        totalPerPerson = totalPerPersonState){}
    }


    }


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Mybill(modifier: Modifier = Modifier,
           Range: IntRange = 1..100,
           SplitByState: MutableState<Int>,
           tipamountstate : MutableState<Double>,
           totalPerPerson: MutableState<Double>,
          onValChange : (String) -> Unit) {

    val TotalBillState = remember {
        mutableStateOf("")
    }
    val ValidState = remember(TotalBillState.value) {
        TotalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current


    val sliderPositionState = remember {
        mutableStateOf(0f)
    }
    val Tippercentage = (sliderPositionState.value * 100).toInt()


    TopHeader(totalPerPerson = totalPerPerson.value)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray), elevation = 6.dp
    ) {
        Column(
            modifier = modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Inputfield(Valuestate = TotalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!ValidState) return@KeyboardActions
                    onValChange(TotalBillState.value.trim())
                    keyboardController?.hide()
                }
            )
            if (ValidState) {
                Row(modifier = modifier.padding(3.dp), horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = "Split",
                        modifier = Modifier.align(alignment = CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(imageVector = Icons.Default.Remove, onclick = {
                            SplitByState.value =
                                if (SplitByState.value > 1) SplitByState.value - 1
                            else 1

                            totalPerPerson.value =
                                CalculateTotalBill(TotalBill = TotalBillState.value.toDouble(),
                                    splitBy = SplitByState.value,
                                    Tippercentage = Tippercentage)




                        })

                        Text(
                            text = "${SplitByState.value}", modifier = Modifier
                                .align(CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )

                        RoundIconButton(imageVector = Icons.Default.Add, onclick = {

                                if(SplitByState.value < Range.last ) SplitByState.value += 1

                            totalPerPerson.value =
                                CalculateTotalBill(TotalBill = TotalBillState.value.toDouble(),
                                    splitBy = SplitByState.value,
                                    Tippercentage = Tippercentage)
                        })
                    }
                }

                Row(modifier = modifier.padding(horizontal = 3.dp, vertical = 12.dp)){
                    Text(
                        text = "Tip",
                        modifier = modifier.align(alignment = Alignment.CenterVertically)
                        
                    )
                    
                    Spacer(modifier = Modifier.width(200.dp))
                    
                    Text(text = "${tipamountstate.value}", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                }
                
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$Tippercentage %")
                    
                    Spacer(modifier = Modifier.height(20.dp))

                    //slider

                    Slider(value = sliderPositionState.value, onValueChange ={newval ->
                        sliderPositionState.value = newval
                        tipamountstate.value =
                            calculateTotalTip(TotalBill = TotalBillState.value.toDouble() , Tippercentage = Tippercentage )
                            totalPerPerson.value =
                                CalculateTotalBill(TotalBill = TotalBillState.value.toDouble(),
                                splitBy = SplitByState.value,
                                Tippercentage = Tippercentage)
                    },
                        modifier = modifier.padding(start = 15.dp, end = 15.dp), steps = 5 )
                }

            } else {
                Box() {

                }
            }
        }

    }
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
        TipCalculatorTheme {
            Myapp {
            }
        }

    }

