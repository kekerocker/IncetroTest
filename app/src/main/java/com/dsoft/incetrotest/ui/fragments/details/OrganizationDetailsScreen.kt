package com.dsoft.incetrotest.ui.fragments.details

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dsoft.domain.model.OrganizationDetails
import com.dsoft.domain.util.Response
import com.dsoft.incetrotest.R
import com.dsoft.incetrotest.ui.theme.backgroundMain
import com.dsoft.incetrotest.ui.theme.colorBackground4
import com.dsoft.incetrotest.ui.theme.colorAccentBlue
import com.dsoft.incetrotest.ui.theme.colorGray
import com.dsoft.incetrotest.ui.utils.LoadingScreen
import kotlin.math.floor

@Composable
fun OrganizationDetailsScreen(
    navController: NavController,
    viewModel: OrganizationDetailsViewModel = hiltViewModel()
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(color = backgroundMain)
    ) {
        val (toolbar, photoRow, organizationInfoCard, descriptionCard) = createRefs()

        MyToolbar(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .constrainAs(ref = toolbar,
                    constrainBlock = {
                        top.linkTo(parent.top)
                    }),
            navController = navController
        )
        val details = viewModel.detailsState.collectAsState()
        val context = LocalContext.current


        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .constrainAs(ref = photoRow, constrainBlock = {
                    top.linkTo(toolbar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        ) {
            when (details.value) {
                is Response.Success -> {
                    details.value?.data?.photos?.also { list ->
                        items(list) { photo ->
                            PhotoCard(photo)
                        }
                    }
                }

                is Response.Failure -> {
                    val value = details.value as Response.Failure
                    Toast.makeText(context, value.exception.message, Toast.LENGTH_SHORT).show()
                }

                else -> Unit
            }


        }
        OrganizationInfoCard(details = details.value?.data, modifier = Modifier.constrainAs(
            ref = organizationInfoCard,
            constrainBlock = {
                top.linkTo(photoRow.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ),
            viewModel = viewModel
        )

        DescriptionCard(details = details.value?.data, modifier = Modifier.constrainAs(
            ref = descriptionCard,
            constrainBlock = {
                top.linkTo(organizationInfoCard.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ))

        if (details.value is Response.Loading) LoadingScreen()
    }
}

@Composable
fun AlertDialogSample() {
    Column {
        val openDialog = remember { mutableStateOf(false) }

        Button(onClick = {
            openDialog.value = true
        }) {
            Text("Click me")
        }

        if (openDialog.value) {

            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = "Dialog Title Will Show Here")
                },
                text = {
                    Text("Here is a description text of the dialog")
                },
                confirmButton = {
                    Button(

                        onClick = {
                            openDialog.value = false
                        }) {
                        Text("Confirm Button")
                    }
                },
                dismissButton = {
                    Button(

                        onClick = {
                            openDialog.value = false
                        }) {
                        Text("Dismiss Button")
                    }
                }
            )
        }
    }

}

@Composable
private fun OrganizationInfoCard(
    details: OrganizationDetails?,
    modifier: Modifier,
    viewModel: OrganizationDetailsViewModel
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(24.dp, 24.dp, 24.dp, 24.dp))
            .background(Color.White)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            val (name, favoriteBtn, ratingBar, ratingValue, averageBill) = createRefs()

            Text(
                modifier = Modifier.constrainAs(ref = name, constrainBlock = {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(favoriteBtn.start, margin = 4.dp)
                    width = Dimension.fillToConstraints
                }),
                text = details?.name.orEmpty(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )

            val favoriteImage =
                if (details?.isFavorite == true) R.drawable.ic_favorite else R.drawable.ic_not_favorite

            Image(
                modifier = Modifier
                    .constrainAs(ref = favoriteBtn, constrainBlock = {
                        top.linkTo(name.top)
                        bottom.linkTo(name.bottom)
                        end.linkTo(parent.end)
                    })
                    .clickable {
                        if (details?.isFavorite == true) viewModel.deleteFromFavorite(details.id)
                        else viewModel.addToFavorite(details?.id ?: 0)
                    },
                painter = painterResource(id = favoriteImage),
                contentDescription = null,
            )

            RatingBar(
                details = details,
                modifier = Modifier.constrainAs(
                    ref = ratingBar,
                    constrainBlock = {
                        top.linkTo(name.bottom, margin = 8.dp)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                ))

            Text(
                text = (details?.rate ?: 0.0).toString(),
                color = colorAccentBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(
                    ref = ratingValue,
                    constrainBlock = {
                        start.linkTo(ratingBar.end, margin = 8.dp)
                        top.linkTo(ratingBar.top)
                        bottom.linkTo(ratingBar.bottom)
                    }
                )
            )

            Text(
                text = "$30",
                color = colorGray,
                modifier = Modifier
                    .constrainAs(
                        ref = averageBill,
                        constrainBlock = {
                            end.linkTo(parent.end)
                            top.linkTo(ratingValue.top)
                            bottom.linkTo(ratingValue.bottom)
                        }
                    )
            )

        }
    }
}

@Composable
private fun DescriptionCard(details: OrganizationDetails?, modifier: Modifier) {
    val detailedInfo =
        details?.detailedInfo.orEmpty().ifBlank { stringResource(id = R.string.empty_description) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(24.dp, 24.dp, 24.dp, 24.dp))
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.description),
                fontWeight = FontWeight.W700
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = detailedInfo,
                color = colorGray,
                fontWeight = FontWeight.W400
            )
        }
    }
}

@Composable
private fun RatingBar(details: OrganizationDetails?, modifier: Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val list = listOf(1, 2, 3, 4, 5)
        val rateCount = floor(details?.rate ?: 0.0).toInt()

        items(list) { star ->
            val tint =
                if (star <= rateCount) colorAccentBlue
                else colorBackground4
            Image(
                modifier = Modifier
                    .width(18.dp)
                    .height(18.dp),
                painter = painterResource(id = R.drawable.ic_star),
                colorFilter = ColorFilter.tint(tint),
                contentDescription = null
            )

        }
    }
}

@Composable
fun PhotoCard(photoUrl: String) {
    AsyncImage(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(24.dp, 24.dp, 24.dp, 24.dp))
            .height(180.dp)
            .width(240.dp),
        contentScale = ContentScale.Crop,
        model = ImageRequest.Builder(LocalContext.current)
            .data(photoUrl)
            .build(),
        contentDescription = null,
    )
}

@Composable
private fun MyToolbar(modifier: Modifier, navController: NavController) {
    ConstraintLayout(
        modifier = modifier
            .background(backgroundMain)
    ) {
        val (navButton, title) = createRefs()

        Image(
            modifier = Modifier
                .clickable { navController.navigateUp() }
                .constrainAs(ref = navButton, constrainBlock = {
                    start.linkTo(parent.start, margin = 16.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }),
            painter = painterResource(id = R.drawable.ic_arrow_backward),
            contentDescription = null
        )

        Text(
            modifier = Modifier.constrainAs(
                ref = title,
                constrainBlock = {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.preferredWrapContent
                }),
            text = stringResource(id = R.string.restaurant),
            fontWeight = FontWeight.Bold
        )
    }
}